package cz.muni.fi.DebugDbAnalyzerApp.Utils;

/**
 * Enum which represents names of columns in debug_log table.
 * @author Miroslav Kubus
 */
public enum ColumnsNames {
    ID(1), LOG(2), INFO(3), LEVEL(4), MODULE(5), PROCESS_NAME(6), 
    PROCESS_ID(7), THREAD_ID(8), DATE_TIME(9);
    
    private final int numVal;
    
    /**
     * Creates one of columns names.
     * @param numVal number of column in table.
     */
    ColumnsNames(int numVal) {
        this.numVal = numVal;
    }
    
    /**
     * Getter for value of specific column name.
     * @return number of column.
     */
    public int getNumVal() {
        return this.numVal;
    }
}
