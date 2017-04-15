package cz.muni.fi.DebugDbAnalyzerApp.Database;

import cz.muni.fi.DebugDbAnalyzerApp.ApplicationUtils.TextAreaLoggerHandler;
import cz.muni.fi.DebugDbAnalyzerApp.DataStorage.DatabaseRow;
import cz.muni.fi.DebugDbAnalyzerApp.DataStorage.ProcessStats;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.*;
import cz.muni.fi.DebugDbAnalyzerApp.XmlOutput.XmlWriterImpl;
import cz.muni.fi.DebugDbAnalyzerApp.XmlOutput.XmlWriter;
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
 * It implements DatabaseAccessManager.
 * @author Miroslav Kubus
 */
public class DatabaseAccessManagerImpl implements DatabaseAccessManager {
    
    private static final Logger LOGGER = Logger.getLogger(DatabaseAccessManagerImpl.class.getName());
    private final DataAnalyzer analyzer;
    private XmlWriter myWriter;
    private String databaseURL;
    private FileWorker fileWorker;
    private BasicDataSource ds;
    
    /**
     * Constructor for constructing DatabaseAccessManagerImpl class. 
     * It also creates instance of DataAnalyzerImpl, XmlWriterImpl and FileWorkerImpl.
     * This constructor sets number of logs around errors according to size of database file.
     * @param pathToDB represents path to chosen database file
     * @param logHandler represents handler for logger to be visualized in frontend
     * @throws ServiceFailureException in case of error during initialization of XmlWriterImpl
     */
    public DatabaseAccessManagerImpl(String pathToDB, TextAreaLoggerHandler logHandler) throws ServiceFailureException {
        this.initDatabaseAccessManagerImpl(pathToDB, logHandler);
        analyzer = new DataAnalyzerImpl(fileWorker.getNumberOfLogsAroundErrors(pathToDB));
    }
    
    /**
     * Constructor for constructing DatabaseAccessManagerImpl class. 
     * It also creates instance of DataAnalyzerImpl, XmlWriterImpl and FileWorkerImpl.
     * This constructor sets number of logs around errors according to logsAround param.
     * @param pathToDB represents path to chosen database file
     * @param logHandler represents handler for logger to be visualized in frontend
     * @param logsAround represents number of logs around error group. -1 represents all logs.
     * @throws ServiceFailureException in case of error during initialization of XmlWriterImpl
     */
    public DatabaseAccessManagerImpl(String pathToDB, TextAreaLoggerHandler logHandler, int logsAround) throws ServiceFailureException {
        this.initDatabaseAccessManagerImpl(pathToDB, logHandler);
        analyzer = new DataAnalyzerImpl(logsAround);
    }
     
    @Override
    public void createIndexOnProcessName() throws ServiceFailureException {
        LOGGER.log(Level.INFO, "Creating database index on processName ...");
        try(Connection con = ds.getConnection();
            Statement statement = con.createStatement()) {
            statement.setQueryTimeout(60);
            statement.executeUpdate("CREATE INDEX if not exists IX_debug_log_processName ON debug_log (process_name)");
            LOGGER.log(Level.INFO, "Database index on processName created!");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while creating index on process_name "
                    + "column in table debug_log in database!", ex);
            throw new ServiceFailureException("Internal error: error while creating "
                    + "index on process_name in database!", ex);
        }
    }
    
    @Override
    public void dropProcessNameIndex() throws ServiceFailureException {
        LOGGER.log(Level.INFO, "Dropping database index on processName ...");
        try(Connection con = ds.getConnection();
            Statement statement = con.createStatement()) {
            statement.executeUpdate("DROP INDEX if exists IX_debug_log_processName");
            LOGGER.log(Level.INFO, "Database index on processName dropped!");
            LOGGER.log(Level.INFO, "Database analyzed!");
        } catch (SQLException ex) {
            LOGGER.log(Level.SEVERE, "Error while dropping index on process_name "
                    + "column in table debug_log in database!", ex);
            throw new ServiceFailureException("Internal error: error while deleting "
                    + "index on process_name in database!", ex);
        }
    }
    
    @Override
    public List<String> getAllProcessNamesFromDatabase() throws ServiceFailureException {
        LOGGER.log(Level.INFO, "Retrieving all unique process names from database ...");
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
        
        LOGGER.log(Level.INFO, "All unique process names from database retrieved!");
        return processNames;
    }
    
    @Override
    public void accessDebugLogTable(List<String> processNames) throws ServiceFailureException {
        try {
            writeStartOfFile();

            for(String name : processNames) {
                accessDebugLogTableByName(name);
            }
            
            writeStatsAndEndOfFile();
        } catch(SQLException ex) {
            throw new ServiceFailureException("Internal error: error while closing "
                    + "ResultSet, statement or connection after accessing the database", ex);
        } 
    }
    
    @Override
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
            LOGGER.log(Level.INFO, "Retrieving logs associated with {0}", name);

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
                        rs.getInt(ColumnsNames.THREAD_ID.getNumVal()), rs.getTimestamp(ColumnsNames.DATE_TIME.getNumVal())));
                    size++;
                }
                analyzer.calculateStatisticsForSpecificProcess(listOfElements, statistics);
                myWriter.writeLogsToOutputDoc(analyzer.analyzeDebugLogTable(listOfElements), statistics);
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
     * Method which executes initialization of class.
     * @param pathToDB represents path to chosen database file
     * @param logHandler represents handler for logger to be visualized in frontend
     * @throws ServiceFailureException 
     */
    private void initDatabaseAccessManagerImpl(String pathToDB, TextAreaLoggerHandler logHandler) throws ServiceFailureException {
        if(LOGGER.getHandlers().length == 0) {
            LOGGER.addHandler(logHandler);
        }
        
        fileWorker = new FileWorkerImpl();
        myWriter = new XmlWriterImpl(fileWorker.getDatabaseFolder(pathToDB), logHandler);
        databaseURL = fileWorker.modifySlashes(pathToDB);
        this.createDataSource(); 
    }
    
    /**
     * Method which create basic data source for access to database.
     */
    private void createDataSource() {
        this.ds = new BasicDataSource();
        ds.setDriverClassName("org.sqlite.JDBC");
        ds.setUrl("jdbc:sqlite:/" + databaseURL);
        LOGGER.log(Level.INFO, "Database connection created!");
    }
       
    /**
     * Method which writes beginning of xml ouput document
     * with beginning tag of element Logs.
     * @throws ServiceFailureException in case of error while writing.
     */
    private void writeStartOfFile() throws ServiceFailureException {
        myWriter.writeStartOfDocument();
        myWriter.writeStartOfElement("Logs");
    }
    
    /**
     * Method which writes stats to xml output together with end of file.
     * @throws ServiceFailureException in case of error while writing.
     */
    private void writeStatsAndEndOfFile() throws ServiceFailureException {
        myWriter.writeEndOfElement();
        myWriter.writeDatabaseStats(analyzer.getDatabaseStatistics().getErrorsCriticalsOfFunctionStats(),
                "FunctionStats");
        myWriter.writeDatabaseStats(analyzer.getDatabaseStatistics().getProcessErrorsCriticalsStats(), 
                "ProcessStats");
        myWriter.writeEndOfDocument();
    }
}