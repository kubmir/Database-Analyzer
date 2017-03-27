package cz.muni.fi.DebugDbAnalyzerApp.DataStorage;

/**
 * Class which stores counts of errors and criticals.
 * @author Miroslav Kubus
 */
public class ErrorAndCriticalStats {
    private int error;
    private int critical;
    
    public ErrorAndCriticalStats(int error, int critical) {
        this.error = error;
        this.critical = critical;
    }

    public int getError() {
        return error;
    }

    public int getCritical() {
        return critical;
    }    

    public void setError(int error) {
        this.error = error;
    }

    public void setCritical(int critical) {
        this.critical = critical;
    }

    @Override
    public String toString() {
        return "ErrorAndCriticalStats{" + "error=" + error + 
                ", critical=" + critical + '}';
    } 
}
