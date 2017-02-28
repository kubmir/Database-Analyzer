package cz.muni.fi.bakalarka1.Database;

/**
 *
 * @author Miroslav Kubus
 */
public class ProcessStats {
    private int verbose;
    private int debug;
    private int info;
    private int warning;
    private int error;
    private int critical;
    private String processName;
    
    public ProcessStats(String name) {
        verbose = 0;
        debug = 0;
        info = 0;
        warning = 0;
        error = 0;
        critical = 0;
        processName = name;
    }

    public int getVerbose() {
        return verbose;
    }

    public int getDebug() {
        return debug;
    }

    public int getInfo() {
        return info;
    }

    public int getWarning() {
        return warning;
    }

    public int getError() {
        return error;
    }

    public int getCritical() {
        return critical;
    }

    public String getProcessName() {
        return processName;
    }

    public void setVerbose(int verbose) {
        if(this.verbose == 0) {
            this.verbose = verbose;
        } else {
            this.verbose += verbose;
        }
    }

    public void setDebug(int debug) {
        if(this.debug == 0) {
            this.debug = debug;
        } else {
            this.debug += debug;
        }
    }

    public void setInfo(int info) {
        if(this.info == 0) {
            this.info = info;
        } else {
            this.info += info;
        }
    }

    public void setWarning(int warning) {
        if(this.warning == 0) {
            this.warning = warning;
        } else {
            this.warning += warning;
        }
    }

    public void setError(int error) {
        if(this.error == 0) {
            this.error = error;
        } else {
            this.error += error;
        }
    }

    public void setCritical(int critical) {
        if(this.critical == 0) {
            this.critical = critical;
        } else {
            this.critical += critical;
        }
    }

    public void setProcessName(String processName) {
        this.processName = processName;
    }
    
    @Override
    public String toString() {
        return "ProcessStats{" + "verbose=" + verbose + ", debug=" + debug + 
                ", info=" + info + ", warning=" + warning + ", error=" + error +
                ", critical=" + critical + ", processName=" + processName + '}';
    }
}
