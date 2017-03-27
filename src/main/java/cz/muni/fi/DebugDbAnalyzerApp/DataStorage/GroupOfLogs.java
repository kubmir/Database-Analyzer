package cz.muni.fi.DebugDbAnalyzerApp.DataStorage;

/**
 * Class which represents rows in database grouped by function in info
 * @author Miroslav Kubus
 */
public class GroupOfLogs {
    private int count;
    private int startID;
    private int endID;
    private int level;
    private String type;
    private int module;
    private int processID;
    private int threadID;
    private String identity;
    private String startDate;
    private String endDate;
    private boolean visible;
    
    public GroupOfLogs(int count, int startID, int endID, int level, int module, 
            int processID, int threadID, String identity, String type, 
            String startDate, String endDate) {
        
        this.count = count;
        this.startID = startID;
        this.endID = endID;
        this.level = level;
        this.module = module;
        this.processID = processID;
        this.threadID = threadID;
        this.identity = identity;
        this.type = type;
        this.startDate = startDate;
        this.endDate = endDate;
        this.visible = false;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
    
      public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @Override
    public String toString() {
        return "GroupOfLogs{" + "count=" + count + ", startID=" + startID + 
                ", endID=" + endID + ", level=" + level + ", type=" + type + 
                ", module=" + module + ", processID=" + processID + 
                ", threadID=" + threadID + ", identity=" + identity + 
                ", startDate=" + startDate + ", endDate=" + endDate + 
                ", visible=" + visible + '}';
    }
}
