package cz.muni.fi.bakalarka1.Database;

/**
 * Class which represents rows in database grouped by function in info
 * @author Miroslav Kubus
 */
public class Result {
    private int count;
    private int startID;
    private int endID;
    private int level;
    private int module;
    private int processID;
    private int threadID;
    private String identity;
    
    public Result(int count, int startID, int endID, int level, int module, int processID, int threadID, String identity) {
        this.count = count;
        this.startID = startID;
        this.endID = endID;
        this.level = level;
        this.module = module;
        this.processID = processID;
        this.threadID = threadID;
        this.identity = identity;
    }

    public void setIdentity(String identity) {
        this.identity = identity;
    }

    public String getIdentity() {
        return identity;
    }
    
    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getStartID() {
        return startID;
    }

    public void setStartID(int startID) {
        this.startID = startID;
    }

    public int getEndID() {
        return endID;
    }

    public void setEndID(int endID) {
        this.endID = endID;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getModule() {
        return module;
    }

    public void setModule(int module) {
        this.module = module;
    }

    public int getProcessID() {
        return processID;
    }

    public void setProcessID(int processID) {
        this.processID = processID;
    }

    public int getThreadID() {
        return threadID;
    }

    public void setThreadID(int threadID) {
        this.threadID = threadID;
    }
}
