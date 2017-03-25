package cz.muni.fi.DebugDbAnalyzerApp.Database;

import cz.muni.fi.DebugDbAnalyzerApp.Utils.DatabaseRow;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.GroupOfLogs;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.ProcessStats;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class which execute several different analyzes on data
 * from database. It implements DataAnalyzer methods.
 * @author Miroslav Kubus
 */
public class DataAnalyzerImpl implements DataAnalyzer {
        
    @Override
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
    
    @Override
    public List<GroupOfLogs> analyzeDebugLogTable(List<DatabaseRow> elements) {
        int count;
        int startID;
        DatabaseRow row;
        String startDate;
        List<GroupOfLogs> results = new ArrayList<>();

        for(int i = 0; i < elements.size(); i++) {
            count = 1;
            startID = elements.get(i).getID();
            startDate = String.valueOf(elements.get(i).getDateTime());
            
            while(i < elements.size() - 1 && elements.get(i).getLevel() == elements.get(i + 1).getLevel() 
                    && elements.get(i).getIdentity().equals(elements.get(i + 1).getIdentity())) {
                count++;
                i++;
            }
                        
            row = elements.get(i);
            this.addResult(results, row, count, startID, startDate);
        }
        
        return Collections.unmodifiableList(filterGroupsAroundErrorAndCritical(results));
    }
    
    @Override
    public List<GroupOfLogs> filterGroupsAroundErrorAndCritical(List<GroupOfLogs> allGroups) {
        int currentPosition = 0;
        
        for (GroupOfLogs group : allGroups) {
            
            if(group.getType().compareTo("Error") == 0 || group.getType().compareTo("Critical") == 0) {
                
                int position = currentPosition - 1;
                //Setting visibility 50 groups before specific error/critical
                while(position >= 0 && position >= currentPosition - 50 && 
                        allGroups.get(position).getType().compareTo("Error") != 0 && 
                        allGroups.get(position).getType().compareTo("Critical") != 0) {
                    
                    allGroups.get(position).setVisible(true);
                    position -= 1;
                }
                
                group.setVisible(true);
                
                position = currentPosition + 1;
                //Setting visibility 50 groups after specific error/critical
                while(position < allGroups.size() && position <= currentPosition + 50 && 
                        allGroups.get(position).getType().compareTo("Error") != 0 && 
                        allGroups.get(position).getType().compareTo("Critical") != 0) {
                    
                    allGroups.get(position).setVisible(true);
                    position += 1;
                }
            }
            
            currentPosition += 1;
        }
        
        return getAllVisibleGroups(allGroups);
    }
    
    @Override
    public List<GroupOfLogs> getAllVisibleGroups(List<GroupOfLogs> allGroups) {
        List<GroupOfLogs> visible = new ArrayList<>();
        
        for(GroupOfLogs group : allGroups) {
            if(group.isVisible()) {
                visible.add(group);
            }
        }
        
        return Collections.unmodifiableList(visible);
    }
    
    /**
     * Method which adds new result to list results
     * @param results represents list to which will be result added
     * @param row represents one of rows from database which 
     * are grouped into one result
     * @param count represents count of logs which are grouped into one result
     * @param startID represents id of first databaseRow in group 
     */
    private void addResult(List<GroupOfLogs> results, DatabaseRow row, int count, int startID, String startDate) {
        String identity;
        
        if(row.getLevelAsString().compareTo("Error") == 0) {
            identity = row.getLog();
        } else {
            identity = "Execution of " + row.getIdentity();
        }
            
        results.add(new GroupOfLogs(count, startID, row.getID(), row.getLevel(),
            row.getModule(), row.getProcessId(), row.getThreadId(), 
            identity, row.getLevelAsString(), startDate, String.valueOf(row.getDateTime())));
    }
}
