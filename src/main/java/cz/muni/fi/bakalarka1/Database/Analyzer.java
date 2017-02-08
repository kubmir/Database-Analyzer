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
    private List<Result> errors;
    
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
     */
    public void analyzeDebugLogTable(List<Result> elements) {
        
    }
    
    /**
     * Method which returns all errors which were detected while analyzes
     * @return list of all errors in database detected while analyzes
     */
    public List<Result> getErrors() {
        return Collections.unmodifiableList(errors);
    }
}
