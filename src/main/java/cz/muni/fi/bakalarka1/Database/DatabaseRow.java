package cz.muni.fi.bakalarka1.Database;

import java.sql.Date;

/**
 * Class which represents one specific error which was found in debug_log 
 * table in database with all attributes of error.
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
    private String identity; //mozno vyuzit pri parsovani pre porovnavanie??
    
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
        this.identity = info.substring(0, 10);
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
    
    public String getIdentity() {
        return this.identity;
    }
    
    @Override
    public String toString() {
        return "Element with id " + this.id + " identity " + this.identity;
    }
}
