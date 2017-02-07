package cz.muni.fi.bakalarka1.Database;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which execute several different analyzes on data
 * from database.
 * @author Miroslav Kubus
 */
public class Analyzer {
    private List<ArrayList> errors;
    
    /**
     * Constructor for class analyzer. It creates new list of errors.
     */
    public Analyzer() {
        this.errors = new ArrayList<>();
    }
    
}
