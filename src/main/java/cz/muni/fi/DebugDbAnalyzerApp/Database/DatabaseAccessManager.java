package cz.muni.fi.DebugDbAnalyzerApp.Database;

import cz.muni.fi.DebugDbAnalyzerApp.DataStorage.DatabaseRow;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface which define methods for access to database.
 * @author Miroslav Kubus
 */
public interface DatabaseAccessManager {

    /**
     * Method which create index on column process_name in table debug_log. 
     * @throws ServiceFailureException in case of error while creating index in database.
     */
    public void createIndexOnProcessName() throws ServiceFailureException;
    
    /**
     * Method which drop index of column process_name in table debug_log. 
     * @throws ServiceFailureException in case of error while dropping index in database.
     */
    public void dropProcessNameIndex() throws ServiceFailureException;
    
    /**
     * Method which retrieve all process names from database.
     * @return list of all unique process names in database.
     * @throws ServiceFailureException in case of error during processing.
     */
    public List<String> getAllProcessNamesFromDatabase() 
            throws ServiceFailureException;
    
    /**
     * Method which access all logs associated with process name 
     * "name" in table debug_log in database.
     * @param name represents name of specific process.
     * @return list of all logs which process name NAME. 
     * @throws ServiceFailureException in case of error while working with database.
     * @throws java.sql.SQLException in case of error while
     * closing connection/statement/resultSet.
     */
    public List<DatabaseRow> accessDebugLogTableByName(String name) 
            throws ServiceFailureException, SQLException;
    
    /**
     * Method which retrieves all logs from database associated with process
     * names in processNames list.
     * @param processNames represents list of all process name which should be analyzed.
     * @throws ServiceFailureException in case of error while retieving data.
     */
    public void accessDebugLogTable(List<String> processNames) 
            throws ServiceFailureException;
    
    /**
     * Check database if contains verbose logs.
     * @return true if database contains verbose logs, false otherwise.
     * @throws ServiceFailureException in case of error while retieving data.
     */
    public boolean containsVerboseLogs() throws ServiceFailureException;
}
