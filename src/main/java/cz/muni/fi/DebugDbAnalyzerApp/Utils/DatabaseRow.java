package cz.muni.fi.DebugDbAnalyzerApp.Utils;

import java.sql.Date;

/**
 * Class which represents one specific row from debug_log 
 * table in database with all attributes of row.
 * @author Miroslav Kubus
 */
public class DatabaseRow {
    
    private int id;
    private String log;
    private String info;
    private int level;
    private int module;
    private String process_name;
    private int process_id;
    private int thread_id;
    private Date date_time;
    private String identity;
    
    public DatabaseRow(int id, String log, String info, int level, int module,
            String processName, int processId, int threadId, Date dateTime) {
        this.id = id;
        this.log = log;
        this.info = info;
        this.level = level;
        this.module = module;
        this.process_name = processName;
        this.process_id = processId;
        this.thread_id = threadId;
        this.date_time = dateTime;
    }
    
    /**
     * Method which transform numeric representation of level
     * to string representation
     * @return string which represents level number 
     */
    public String getLevelAsString() {
        if(this.level == 100) {
            return "Verbose";
        }
        
        if(this.level == 200) {
            return "Debug";
        }
        
        if(this.level == 400) {
            return "Info";
        }
        
        if(this.level == 600) {
            return "Warning";
        }
        
        if(this.level == 800) {
            return "Error";
        }
        
        if(this.level == 1000) {
            return "Critical";
        }
        
        return "UNKOWN LEVEL";
    }
    
    public void setID(int id) {
        this.id = id;
    }
    
    public void setLog(String log) {
        this.log = log;
    }
    
    public void setInfo(String info) {
        this.info = info;
    }
    
    public void setLevel(int level) {
        this.level = level;
    }
    
    public void setModule(int module) {
        this.module = module;
    }
    
    public void setProcessName(String processName) {
        this.process_name = processName;
    }
    
    public void setProcessId(int processId) {
        this.process_id = processId;
    } 
    
    public void setThreadId(int threadId) {
        this.thread_id = threadId;
    }
    
    public void setDateTime(Date date) {
        this.date_time = date;
    }
    
    public int getID() {
        return this.id;
    }
    
    public String getLog() {
        return this.log;
    }
    
    public String getInfo() {
        return this.info;
    }
    
    public int getLevel() {
        return this.level;
    }
    
    public int getModule() {
        return this.module;
    }
    
    public String getProcessName() {
        return this.process_name;
    }
    
    public int getProcessId() {
        return this.process_id;
    } 
    
    public int getThreadId() {
        return this.thread_id;
    }
    
    public Date getDateTime() {
        return this.date_time;
    }  
    
    /**
     * Method which parse name of function which caused 
     * insertion of row into database table debug_log - it parses attribute info 
     * and delete number of line of code from info.
     * @return string representation of function without line number
     */
    public String getIdentity() {        
        return info.replaceAll("\\[.*?\\]", "");
    }

    @Override
    public String toString() {
        return "DatabaseRow{" + "id=" + id + ", log=" + log + ", info=" + info 
                + ", level=" + level + ", module=" + module + ", process_name="
                + process_name + ", process_id=" + process_id + ", thread_id=" 
                + thread_id + ", date_time=" + date_time + ", identity=" 
                + identity + '}';
    }
}
