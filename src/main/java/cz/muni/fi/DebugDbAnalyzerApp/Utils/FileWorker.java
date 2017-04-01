package cz.muni.fi.DebugDbAnalyzerApp.Utils;

/**
 * Interface for working with path to database and size of database.
 * @author Miroslav Kubus
 */
public interface FileWorker {

    /**
     * Method which return number of logs around errors and criticals
     * group. If database is bigger then less logs are writen to XML.
     * @param path represents path to database
     * @return number of logs around errors and criticals
     */
    public int getNumberOfLogsAroundErrors(String path);
    
    /**
     * This method change file path to url path - WINDOWS: replace \ with /
     * @param stringToModify represents path to .db file
     * @return string where all \ are replaced with /
     */
    public String modifySlashes(String stringToModify);
}
