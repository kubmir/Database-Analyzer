package cz.muni.fi.bakalarka1.Database;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;


/**
 * Class which execute several different analyzes on data
 * from database.
 * @author Miroslav Kubus
 */
public class Analyzer {
    
    public Map<String, Integer> calculateStatisticsForSpecificProcess(List<DatabaseRow> elements, Map<String, Integer> results) {
        int level;
        int verbose = results.get("Verbose");
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
        
        results.put("Verbose", verbose);
        results.put("Debug", debug);
        results.put("Info", info);
        results.put("Warning", warning);
        results.put("Error", error);
        results.put("Critical", critical);
        
        return results;
    }
    
    /**
     * Method which analyzes list elements. In case of sequence of DatabaseRows
     * with same identity (info without line number between[]) and level it 
     * groups these DatabaseRows to one and calculate count of them.
     * @param elements represents logs from database to be analyzed
     * @return list of Result to be written to XML
     */
    public List<Result> analyzeDebugLogTable(List<DatabaseRow> elements) {
        int count;
        List<Result> results = new ArrayList<>();
        int startID;
        DatabaseRow row;
        
        for(int i = 0; i < elements.size(); i++) {
            count = 1;
            startID = elements.get(i).getID();
            
            while(i < elements.size() - 1 && elements.get(i).getLevel() == elements.get(i + 1).getLevel() 
                    && elements.get(i).getIdentity().equals(elements.get(i + 1).getIdentity())) {
                count++;
                i++;
            }
            
            row = elements.get(i);
            results.add(new Result(count, startID, row.getID(), row.getLevel(),
                    row.getModule(), row.getProcessId(), row.getThreadId(), "Execution of " + row.getIdentity()));
        }
        
        return Collections.unmodifiableList(results);
    }
}
