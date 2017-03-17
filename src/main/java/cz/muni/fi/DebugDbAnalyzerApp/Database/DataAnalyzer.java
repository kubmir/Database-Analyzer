package cz.muni.fi.DebugDbAnalyzerApp.Database;

import cz.muni.fi.DebugDbAnalyzerApp.Utils.DatabaseRow;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.GroupOfLogs;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.ProcessStats;
import java.util.List;

/**
 * Interface which define methods for analyzing data from database.
 * @author Miroslav Kubus
 */
public interface DataAnalyzer {
    
    /**
     * Method which calculates counts of rows from database with specific
     * level value and same process name. 
     * @param elements represents list of rows from database with same process name
     * @param statistics stores all statistics for specific process name
     */
    public void calculateStatisticsForSpecificProcess(List<DatabaseRow> elements, ProcessStats statistics);
    
    /**
     * Method which analyzes list elements. In case of sequence of DatabaseRows
     * with same identity (info without line number between[]) and level it 
     * groups these DatabaseRows to one and calculate counts of them.
     * @param elements represents rows from database to be analyzed
     * @return list of GroupOfLogs to be written to XML
     */
    public List<GroupOfLogs> analyzeDebugLogTable(List<DatabaseRow> elements);    
}
