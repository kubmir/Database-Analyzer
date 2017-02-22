package cz.muni.fi.bakalarka1.Database;

import cz.muni.fi.bakalarka1.Utils.XMLWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class which execute several different analyzes on data
 * from database.
 * @author Miroslav Kubus
 */
public class Analyzer {
    private List<DatabaseRow> errors;
    
    /**
     * Constructor for class analyzer. It creates new list of errors.
     */
    public Analyzer() {
        this.errors = new ArrayList<>();
    }
    
    /**
     * Method which analyzes list elements. In case of error in list elements it 
     * adds this error into errors list
     * @param elements represents logs from database to be analyzed
     * @return 
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
                    && elements.get(i).getInfo().equals(elements.get(i + 1).getInfo())) {
                count++;
                i++;
            }
            
            row = elements.get(i);
            results.add(new Result(count, startID, row.getID(), row.getLevel(),
                    row.getModule(), row.getProcessId(), row.getThreadId(), "Execution of" + row.getInfo()));
        }
        
        return results;
    }
    
    /**
     * Method which returns all errors which were detected while analyzes
     * @return list of all errors in database detected while analyzes
     */
    public List<DatabaseRow> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
