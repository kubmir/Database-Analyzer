package cz.muni.fi.DebugDbAnalyzerApp.Database;

import cz.muni.fi.DebugDbAnalyzerApp.DataStorage.DatabaseRow;
import cz.muni.fi.DebugDbAnalyzerApp.DataStorage.GroupOfLogs;
import cz.muni.fi.DebugDbAnalyzerApp.DataStorage.ProcessStats;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class which execute several different analyzes on data
 * from database. It implements DataAnalyzer methods.
 * @author Miroslav Kubus
 */
public class DataAnalyzerImpl implements DataAnalyzer {
        
    private final DatabaseStatsImpl databaseStatistics;
 
    /**
     * Constructor for DataAnalyzerImpl class. It creates an instance of 
     * DatabaseStatsImpl class.
     */
    public DataAnalyzerImpl() {
        this.databaseStatistics = new DatabaseStatsImpl();
    }
    
    @Override
    public void calculateStatisticsForSpecificProcess(List<DatabaseRow> elements, ProcessStats statistics) {
        String process = elements.get(0).getProcessName();
        
        if(statistics.getProcessName().compareTo(process) == 0) {
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
            databaseStatistics.addErrorsOfProcessCount(process, error);
            databaseStatistics.addCriticalsOfProcessCount(process, critical);
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
            row = elements.get(i);
            count = 1;
            startID = row.getID();
            startDate = String.valueOf(row.getDateTime());

            if(row.getLevelAsString().compareTo("Error") == 0) {
                databaseStatistics.addErrorOfFunctionCount(row.getIdentity());
            }
            
            if(row.getLevelAsString().compareTo("Critical") == 0) {
                databaseStatistics.addCriticalOfFunctionCount(row.getIdentity());
            }

            while(i < elements.size() - 1 && row.getLevel() == elements.get(i + 1).getLevel() 
                    && row.getIdentity().equals(elements.get(i + 1).getIdentity())) {
                count++;
                i++;
            }
                        
            addResult(results, elements.get(i), count, startID, startDate);
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
    
    @Override
    public DatabaseStatsImpl getDatabaseStatistics() {
        return this.databaseStatistics;
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
            identity = row.getIdentity() + " -> " + row.getLog();
        } else {
            identity = row.getIdentity();
        }
            
        results.add(new GroupOfLogs(count, startID, row.getID(), row.getLevel(),
            row.getModule(), row.getProcessId(), row.getThreadId(), 
            identity, row.getLevelAsString(), startDate, String.valueOf(row.getDateTime())));
    }
}
