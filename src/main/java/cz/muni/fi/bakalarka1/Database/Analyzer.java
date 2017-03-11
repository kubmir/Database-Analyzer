package cz.muni.fi.bakalarka1.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class which execute several different analyzes on data
 * from database.
 * @author Miroslav Kubus
 */
public class Analyzer {
        
    /**
     * Method which calculates counts of rows from database with specific
     * level value and same process name. 
     * @param elements represents list of rows from database with same process name
     * @param statistics stores all statistics for specific process name
     */
    public void calculateStatisticsForSpecificProcess(List<DatabaseRow> elements, ProcessStats statistics) {
        if(statistics.getProcessName().compareTo(elements.get(0).getProcessName()) == 0) {
            int level;
            int verbose = 0;
            int debug = 0;
            int info = 0;
            int warning = 0;
            int error = 0;
            int critical = 0;

            for(DatabaseRow row : elements) {
                level = row.getLevel();

                if(level == 100) { 
                    verbose++;
                }

                if(level == 200) {
                    debug++;
                }

                if(level == 400) {
                    info++;
                }

                if(level == 600) {
                    warning++;
                }

                if(level == 800) {
                    error++;
                }

                if(level == 1000) {
                    critical++;
                }
            }

            statistics.setCritical(critical);
            statistics.setDebug(debug);
            statistics.setError(error);
            statistics.setInfo(info);
            statistics.setVerbose(verbose);
            statistics.setWarning(warning);
        }
    }
    
    /**
     * Method which analyzes list elements. In case of sequence of DatabaseRows
     * with same identity (info without line number between[]) and level it 
     * groups these DatabaseRows to one and calculate counts of them.
     * @param elements represents rows from database to be analyzed
     * @return list of GroupOfLogs to be written to XML
     */
    public List<GroupOfLogs> analyzeDebugLogTable(List<DatabaseRow> elements) {
        int count;
        int startID;
        DatabaseRow row;
        List<GroupOfLogs> results = new ArrayList<>();

        for(int i = 0; i < elements.size(); i++) {
            count = 1;
            startID = elements.get(i).getID();
            
            while(i < elements.size() - 1 && elements.get(i).getLevel() == elements.get(i + 1).getLevel() 
                    && elements.get(i).getIdentity().equals(elements.get(i + 1).getIdentity())) {
                count++;
                i++;
            }
                        
            row = elements.get(i);
            this.addResult(results, row, count, startID);
        }
        
        return Collections.unmodifiableList(results);
    }
    
    /**
     * Method which adds new result to list results
     * @param results represents list to which will be result added
     * @param row represents one of rows from database which 
     * are grouped into one result
     * @param count represents count of logs which are grouped into one result
     * @param startID represents id of first databaseRow in group 
     */
    private void addResult(List<GroupOfLogs> results, DatabaseRow row, int count, int startID) {
        String identity;
        
        if(row.getLevelAsString().compareTo("Error") == 0) {
          identity = row.getLog();
        } else {
            identity = "Execution of " + row.getIdentity();
        }
            
        results.add(new GroupOfLogs(count, startID, row.getID(), row.getLevel(),
            row.getModule(), row.getProcessId(), row.getThreadId(), 
            identity, row.getLevelAsString()));
    }
}
