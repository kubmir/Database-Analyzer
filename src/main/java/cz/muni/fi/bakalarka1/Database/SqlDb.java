package cz.muni.fi.bakalarka1.Database;

import cz.muni.fi.bakalarka1.Utils.ColumnsNames;
import cz.muni.fi.bakalarka1.Utils.FasterXmlWriter;
import cz.muni.fi.bakalarka1.Utils.ServiceFailureException;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 * Class which provide access to database file defined by absolute path.
 * @author Miroslav Kubus
 */
public class SqlDb {
    
    private static final Logger LOGGER = Logger.getLogger(SqlDb.class.getName());
    private final Analyzer analyzer;
    private final FasterXmlWriter myWriter;
    private final String databaseURL;
    private BasicDataSource ds;
    
    /**
     * Constructor for constructing SqlDb class. It also creates instance of Analyzer.
     * @param pathToDB represents path to chosen database file
     * @throws ServiceFailureException in case of error during initialization of XmlWriter
     */
    public SqlDb(String pathToDB) throws ServiceFailureException {
        analyzer = new Analyzer();
        myWriter = new FasterXmlWriter();
        databaseURL = this.modifySlashes(pathToDB);
        this.createDataSource();
    }
    
    /**
     * Method which create basic data source for access to database.
     */
    private void createDataSource() {
        this.ds = new BasicDataSource();
        ds.setDriverClassName("org.sqlite.JDBC");
        ds.setUrl("jdbc:sqlite:/" + databaseURL);
    }
        
    /**
     * Method which create index on column process_name in table debug_log 
     * @throws ServiceFailureException in case of error while creating index in database
     */
    public void createIndex() throws ServiceFailureException {
        try(Connection con = ds.getConnection();
            Statement statement = con.createStatement()) {
            statement.executeUpdate("CREATE INDEX if not exists IX_debug_log_processName ON debug_log (process_name)");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while creating index on process_name "
                    + "column in table debug_log in database!", ex);
            throw new ServiceFailureException("Internal error: error while creating "
                    + "index on process_name in database!", ex);
        }
    }
    
    /**
     * Method which create index on column process_name in table debug_log 
     * @throws ServiceFailureException in case of error while creating index in database
     */
    private void dropIndex() throws ServiceFailureException {
         try(Connection con = ds.getConnection();
            Statement statement = con.createStatement()) {
            statement.executeUpdate("DROP INDEX if exists IX_debug_log_processName");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while dropping index on process_name "
                    + "column in table debug_log in database!", ex);
            throw new ServiceFailureException("Internal error: error while deleting "
                    + "index on process_name in database!", ex);
        }
    }
    
    /**
     * Method which retrieve all process names from database.
     * @return list of all unique process names in database.
     * @throws ServiceFailureException in case of error during processing.
     */
    public List<String> getAllProcessNamesFromDatabase() throws ServiceFailureException {
        List<String> processNames = new ArrayList<>();
        
        try(Connection con = ds.getConnection();
            Statement statement = con.createStatement();
            ResultSet rs = statement.executeQuery("SELECT process_name FROM debug_log GROUP BY process_name")) {
         
            while(rs.next()) {
                processNames.add(rs.getString(1));
            }
            
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while getting all process names from database!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "retrieving all process names from database!", ex);
        }
        
        return processNames;
    }
    
    /**
     * Method whiche access tables in database
     * @throws ServiceFailureException in case of error while working with database
     */    
    public void testAccessDB() throws ServiceFailureException {
        try {
            this.createDataSource();
            this.createIndex();
            myWriter.writeStartOfDocument();
            //this.accessDebugLogTableByName("NetMonitorServer.dll");
            for(String name : this.getAllProcessNamesFromDatabase()) {
               accessDebugLogTableByName(name);
            }
            myWriter.writeEndOfDocument();
        } catch(SQLException ex) {
            throw new ServiceFailureException("Internal error: error while closing "
                    + "ResultSet, statement or connection after accessing the database", ex);
        }
    }
    
     /**
     * Method which access all logs in table debug_log in database
     * @param name represents name of specific process
     * @return list of all logs which process name NAME 
     * @throws ServiceFailureException in case of error while working with database
     * @throws java.sql.SQLException in case of error while closing connection/statement/resultSet
     */
    public List<DatabaseRow> accessDebugLogTableByName(String name) throws ServiceFailureException, SQLException {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        int offset = 0;
        int i = 0;
        int size = 15000;
        List<DatabaseRow> listOfElements = new ArrayList<>();
        ProcessStats statistics = new ProcessStats(name);
        
        try {
            con = ds.getConnection();
           
            while(size == 15000) {
                statement = con.prepareStatement("SELECT * FROM debug_log WHERE process_name = ? LIMIT 15000 OFFSET ?");
                statement.setFetchSize(100);
                statement.setString(1, name);
                statement.setInt(2, offset + (i * 15000));
                rs = statement.executeQuery();
                size = 0;

                while(rs.next()) {                   
                    listOfElements.add(new DatabaseRow(rs.getInt(ColumnsNames.ID.getNumVal()),
                        rs.getString(ColumnsNames.LOG.getNumVal()), rs.getString(ColumnsNames.INFO.getNumVal()),
                        rs.getInt(ColumnsNames.LEVEL.getNumVal()), rs.getInt(ColumnsNames.MODULE.getNumVal()), 
                        rs.getString(ColumnsNames.PROCESS_NAME.getNumVal()), rs.getInt(ColumnsNames.PROCESS_ID.getNumVal()), 
                        rs.getInt(ColumnsNames.THREAD_ID.getNumVal()), rs.getDate(ColumnsNames.DATE_TIME.getNumVal())));
                    size++;
                }
                analyzer.calculateStatisticsForSpecificProcess(listOfElements, statistics);
                System.out.println(statistics);
                myWriter.writeDataToDoc(analyzer.analyzeDebugLogTable(listOfElements), statistics);
                listOfElements.clear();
                System.gc();
                i++;
            }
        } catch(SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while getting rows from database"
                    + " with process name " + name + "!", ex);
            throw new ServiceFailureException("Internal error: problem while "
                    + "executing SQL statement!", ex);
        } finally {
            if(rs != null) {
                rs.close();
            }
            if(statement != null) {
                statement.close();
            }
            
            if(con != null) {
                con.close();
            }
        }
        
        return listOfElements;
    }
    
    /**
     * This method change file path to url path - WINDOWS: replace \ with /
     * @param stringToModify represents path to .db file
     * @return string where all \ are replaced with /
     */
    private String modifySlashes(String stringToModify) {
        return stringToModify.replace(File.separator, "/");
    }
}