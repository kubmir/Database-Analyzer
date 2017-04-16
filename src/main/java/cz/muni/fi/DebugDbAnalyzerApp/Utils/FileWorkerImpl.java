package cz.muni.fi.DebugDbAnalyzerApp.Utils;

import cz.muni.fi.DebugDbAnalyzerApp.ApplicationUtils.TextAreaLoggerHandler;
import cz.muni.fi.DebugDbAnalyzerApp.XmlOutput.XSLTProcessorImpl;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class which works with database files and returns informations
 * according to database file size and path. It implements
 * FileWorker interface.
 * @author Miroslav Kubus
 */
public class FileWorkerImpl implements FileWorker {

    private static final Logger LOGGER = Logger.getLogger(FileWorkerImpl.class.getName());
    private String dataFolderPath;
    
    public FileWorkerImpl(TextAreaLoggerHandler textAreaHandler) {
        LOGGER.addHandler(textAreaHandler);
        dataFolderPath = null;
    }
    
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
    
    @Override
    public String getDatabaseFolder(String pathToDB) {
        return pathToDB.substring(0, pathToDB.lastIndexOf(File.separator));
    }
    
    @Override
    public void ExportResource(String resourceName) throws ServiceFailureException {

        try(InputStream stream = FileWorkerImpl.class.getResourceAsStream("/" + resourceName);
            OutputStream resStreamOut = new FileOutputStream(dataFolderPath + File.separator + resourceName)){

            if(stream != null)
                 LOGGER.log(Level.INFO, "Input stream: {0}", stream.toString());
               
            LOGGER.log(Level.INFO, "Output stream: {0}", dataFolderPath + File.separator + resourceName);
               
            if(stream == null) {
                throw new ServiceFailureException("Cannot get resource \"" + resourceName + "\" from Jar file.");
            }

            int readBytes;
            byte[] buffer = new byte[4096];
            while ((readBytes = stream.read(buffer)) > 0) {
                resStreamOut.write(buffer, 0, readBytes);
            }
        } catch (ServiceFailureException | IOException ex) {
            throw new ServiceFailureException("Error while exporting resources!", ex);
        }
    }
    
    @Override
    public String createDataDirectory() throws ServiceFailureException {
        File directory = null;
        
        try {
            String jarFolder = new File(FileWorkerImpl.class.getProtectionDomain().getCodeSource()
                            .getLocation().toURI().getPath()).getParentFile()
                            .getPath().replace('\\', '/');
            directory = new File(jarFolder + File.separator + "Analyzer_Data");
             
            if(!directory.exists()) {
                directory.mkdir();
            }
        } catch (URISyntaxException ex) {
            throw new ServiceFailureException("Error while creating data folder!", ex);
        }
        
        dataFolderPath = directory.getAbsolutePath();
        return dataFolderPath;
    }
}
