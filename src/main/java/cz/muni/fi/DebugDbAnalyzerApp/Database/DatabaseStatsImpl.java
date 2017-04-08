package cz.muni.fi.DebugDbAnalyzerApp.Database;

import cz.muni.fi.DebugDbAnalyzerApp.DataStorage.ErrorAndCriticalStats;
import static java.util.Collections.unmodifiableMap;
import java.util.HashMap;
import java.util.Map;

/**
 * Class which represents statistics about whole database. It stores
 * process name and count of errors caused by process with specific 
 * process name. It implements DatabaseStats methods.
 * @author Miroslav Kubus
 */
public class DatabaseStatsImpl implements DatabaseStats {
    
    private final Map<String, ErrorAndCriticalStats> processErrorsCount;
    private final Map<String, ErrorAndCriticalStats> errorsOfFunctionCount;
    
    public DatabaseStatsImpl() {
        processErrorsCount = new HashMap<>();
        errorsOfFunctionCount = new HashMap<>();
    }
    
    @Override
    public void addErrorsOfProcessCount(String process, int count) {
        addToMap(process, count, 0, processErrorsCount);
    }
    
    @Override
    public void addCriticalsOfProcessCount(String process, int count) {
        addToMap(process, 0, count, processErrorsCount);
    }
    
    @Override
    public void addErrorOfFunctionCount(String errorFunction) {
        addToMap(errorFunction, 1, 0, errorsOfFunctionCount);
    }
    
    @Override
    public void addCriticalOfFunctionCount(String criticalFunction) {
        addToMap(criticalFunction, 1, 0, errorsOfFunctionCount);
    }
    
    /**
     * Method which add new entity to map. If key is in map than value is updated.
     * @param key represents key used in map
     * @param count represents value used in map
     * @param map  represents map to which method adds data
     */
    private void addToMap(String key, int error, int critical, Map<String, ErrorAndCriticalStats> map) {
        ErrorAndCriticalStats stats;
        
        if(map.containsKey(key)) {
            stats = map.get(key);
            stats.setCritical(stats.getCritical() + critical);
            stats.setError(stats.getError() + error);
        } else {
            stats = new ErrorAndCriticalStats(error, critical);
        } 
        
        map.put(key, stats);
    }
    
    @Override
    public Map<String, ErrorAndCriticalStats> getProcessErrorsCriticalsStats() {
        return unmodifiableMap(processErrorsCount);
    }
    
    @Override
    public Map<String, ErrorAndCriticalStats> getErrorsCriticalsOfFunctionStats() {
        return unmodifiableMap(errorsOfFunctionCount);
    }

    @Override
    public String toString() {
        return "DatabaseStatsImpl{" + "processErrorsCount=" + processErrorsCount 
                + ", errorsOfFunctionCount=" + errorsOfFunctionCount + '}';
    }
}