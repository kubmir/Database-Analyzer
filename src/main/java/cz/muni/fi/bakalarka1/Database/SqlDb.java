package cz.muni.fi.bakalarka1.Database;

import cz.muni.fi.bakalarka1.Utils.ColumnsNames;
import cz.muni.fi.bakalarka1.Utils.ServiceFailureException;
import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Class which provide access to database file defined by absolute path.
 * @author Miroslav Kubus
 */
public class SqlDb {
    
    private Analyzer analyzer;
    
    /**
     * Constructor for constructing SqlDb class. It also creates instance of Analyzer.
     */
    public SqlDb() {
        analyzer = new Analyzer();
    }
    
    /**
     * Method which load sqlite jdbc driver's class file into the memory,
     * instantiate and register this driver.
     * @throws ServiceFailureException in case of exception during loading driver
     */
    public void registerJDBCDriver() throws ServiceFailureException {
        try {
            Class.forName("org.sqlite.JDBC");
        } 
        catch (ClassNotFoundException ex) {
            throw new ServiceFailureException("Internal error: problem while loading driver class!", ex);
        }
    } 
    
    /**
     * Method which establish connection to database file
     * @param pathToDB path to chosen database file
     * @return connection to database file defined by pathToDB. Null in case of error
     * @throws ServiceFailureException in case of error during getting connection
     */
    public Connection establishDBConnection(String pathToDB) throws ServiceFailureException {
        String urlPath = this.modifySlashes(pathToDB);
        
        if(urlPath != null) {
            try {
                Connection con = DriverManager.getConnection("jdbc:sqlite:/" + urlPath); //username a password??
                return con;
            } 
            catch (SQLException ex) {
                throw new ServiceFailureException("Internal error: unable to "
                        + "establish database connection!", ex);
            }
        } else {
            throw new ServiceFailureException("Internal error: invalid file path! "
                    + "File path for establishing database connection is NULL.");
        }
    }
    
    /**
     * Method whiche access tables in database
     * @param pathToDB represents absolute path to .db file
     * @throws ServiceFailureException in case of error while working with database
     */
    public void testAccessDB(String pathToDB) throws ServiceFailureException {
        try {
            accessDebugLogTable(pathToDB);
        } catch(SQLException ex) {
            throw new ServiceFailureException("Internal error: error while closing "
                    + "ResultSet, statement or connection after accessing the database", ex);
        }
    }
    
    /**
     * Method which access all logs in table debug_log in database
     * @param pathToDB represents absolute path to .db file
     * @throws ServiceFailureException in case of error while working with database
     * @throws java.sql.SQLException in case of error while closing connection/statement/resultSet
     */
    public void accessDebugLogTable(String pathToDB) throws ServiceFailureException, SQLException {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        int offset = 0;
        int i = 0;
        int size = 15000;
        List<Result> listOfElements = new ArrayList<>();

        try {
            con = this.establishDBConnection(pathToDB);
            
            while(size == 15000) {
                statement = con.prepareStatement("SELECT * FROM debug_log LIMIT 15000 OFFSET ?");
                statement.setFetchSize(1000); //Zrychlenie o 1000ms cca
                statement.setInt(1, offset + (i * 15000));
                rs = statement.executeQuery();
                size = 0;
                
                while(rs.next()) {                   
                    listOfElements.add(new Result(rs.getInt(ColumnsNames.ID.getNumVal()),
                        rs.getString(ColumnsNames.LOG.getNumVal()), rs.getString(ColumnsNames.INFO.getNumVal()),
                        rs.getInt(ColumnsNames.LEVEL.getNumVal()), rs.getInt(ColumnsNames.MODULE.getNumVal()), 
                        rs.getString(ColumnsNames.PROCESS_NAME.getNumVal()), rs.getInt(ColumnsNames.PROCESS_ID.getNumVal()), 
                        rs.getInt(ColumnsNames.THREAD_ID.getNumVal()), rs.getDate(ColumnsNames.DATE_TIME.getNumVal())));
                    System.out.println(rs.getInt(ColumnsNames.ID.getNumVal()));
                    size++;
                }
                analyzer.analyzeDebugLogTable(listOfElements);
                listOfElements.clear();
                System.gc();
                i++;
            }
        } catch(SQLException ex) {
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
    }

    /**
     * This method change file path to url path - WINDOWS: replace \ with /
     * @param stringToModify represents path to .db file
     * @return string where all \ are replaced with /
     */
    private String modifySlashes(String stringToModify) {
        return stringToModify.replace(File.separator, "/");
    }
    
     /**
     * Method which access all logs in table debug_log in database - LEN POKUS
     * @param pathToDB represents absolute path to .db file
     * @throws ServiceFailureException in case of error while working with database
     */
    public void testAccessDBusingList(String pathToDB) throws ServiceFailureException, SQLException {
        Connection con = null;
        PreparedStatement statement = null;
        ResultSet rs = null;
        List<Result> listOfElements = new ArrayList<>();
        
        try {
            con = this.establishDBConnection(pathToDB);
            statement = con.prepareStatement("SELECT * FROM debug_log");
            rs = statement.executeQuery();
                           
            while(rs.next()) {
                //System.out.println(res);
                listOfElements.add(new Result(rs.getInt(1),rs.getString(2),rs.getString(3),
                rs.getInt(4), rs.getInt(5), rs.getString(6), rs.getInt(7), 
                rs.getInt(8), rs.getDate(9)));
            }
            
        } catch(SQLException ex) {
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
    }
}