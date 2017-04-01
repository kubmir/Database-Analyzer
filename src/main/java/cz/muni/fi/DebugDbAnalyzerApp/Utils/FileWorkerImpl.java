package cz.muni.fi.DebugDbAnalyzerApp.Utils;

import java.io.File;

/**
 * Class which works with database files and returns informations
 * according to database file size and path. It implements
 * FileWorker interface.
 * @author Miroslav Kubus
 */
public class FileWorkerImpl implements FileWorker {
    
    @Override
    public int getNumberOfLogsAroundErrors(String path) {
        File databaseFile = new File(path);
        long databaseSizeInBytes = databaseFile.length();
        long databaseSizeInKB = databaseSizeInBytes / 1024;
        long databaseSizeInMB = databaseSizeInKB / 1024;
        
        if(databaseSizeInMB <= 10) {
            return -1;
        }
        
        if(databaseSizeInMB <= 50) {
            return 150;
        }
        
        if(databaseSizeInMB <= 100) {
            return 100;
        }

        return 50;
    }
    
    @Override
    public String modifySlashes(String stringToModify) {
        return stringToModify.replace(File.separator, "/");
    }
}
