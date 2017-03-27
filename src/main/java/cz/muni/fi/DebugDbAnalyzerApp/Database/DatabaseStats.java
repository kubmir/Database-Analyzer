package cz.muni.fi.DebugDbAnalyzerApp.Database;

import cz.muni.fi.DebugDbAnalyzerApp.DataStorage.ErrorAndCriticalStats;
import java.util.Map;

/**
 * Interface for database statistics.
 * @author Miroslav Kubus
 */
public interface DatabaseStats {
    
    /**
     * Method which add new process with count of errors caused by process.
     * If process is already in map method makes sum of current count and count in map.
     * @param process represents name of specific process
     * @param count represents number of errors caused by process
     */
    public void addErrorsOfProcessCount(String process, int count);

    /**
     * Method which add new error with count 1 to statistic map.
     * If error is already in statistic than count is updated by addition of one. 
     * @param errorFunction represents name of function which caused error
     */
    public void addErrorOfFunctionCount(String errorFunction);

    /**
     * Method which add new process with count of criticals caused by process.
     * If process is already in map method makes sum of current count and count in map.
     * @param process represents name of specific process
     * @param count represents number of criticals caused by process
     */
    public void addCriticalsOfProcessCount(String process, int count);
    
    /**
     * Method which add new critical with count 1 to statistic map.
     * If error is already in statistic than count is updated by addition of one. 
     * @param criticalFunction represents name of function which caused error
     */
    public void addCriticalOfFunctionCount(String criticalFunction);

    /**
     * Method which returns map of processes with count of errors caused by 
     * specific process.
     * @return map of process names and error counts
     */
    public Map<String, ErrorAndCriticalStats> getProcessErrorsCriticalsStats();
    
    /**
     * Method which returns map of errors with count of errors caused by 
     * specific function.
     * @return map of function names and error counts
     */
    public Map<String, ErrorAndCriticalStats> getErrorsCriticalsOfFunctionStats();
}
