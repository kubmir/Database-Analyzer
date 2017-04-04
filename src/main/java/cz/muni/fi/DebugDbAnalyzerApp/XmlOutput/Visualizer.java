package cz.muni.fi.DebugDbAnalyzerApp.XmlOutput;

import cz.muni.fi.DebugDbAnalyzerApp.Utils.FileWorker;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.FileWorkerImpl;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;
import java.io.File;

/**
 * Class which provides operations for transforming xml file
 * to html file and opens html file in default browser of computer.
 * @author Miroslav Kubus
 */
public class Visualizer {

    private static final String PATH_TO_XSLT = "src" + File.separator 
            + "main" + File.separator + "resources" + File.separator + "XSLT.xsl";
    
    
    private final FileWorker fileWorker;
    private String pathToXML;
    private String pathToHtml;
 
    
    public Visualizer(String pathToDB) {
        fileWorker = new FileWorkerImpl();
        pathToHtml = "src" + File.separator + "main" + File.separator 
                + "resources" + File.separator + "htmlOutput.html";
        
        if(pathToDB != null) {
            pathToXML = fileWorker.getDatabaseFolder(pathToDB) 
                + File.separator + "xmlOutput.xml";
        } else {
            pathToXML = null;
        }
    }
    
    /**
     * Method which transform xml output to html output using xslt template.
     * @throws ServiceFailureException in case of error while transformation.
     */
    public void toWeb() throws ServiceFailureException {
        if(pathToXML != null) {
            XSLTProcessor pro = new XSLTProcessor();
            pro.transformToHtml(PATH_TO_XSLT, pathToXML, pathToHtml);
            pro.openHtml(pathToHtml);
        }
    }
    
    public void setPathToXML(String pathToXML) {
        this.pathToXML = pathToXML;
    }

    public void setPathToHtml(String pathToHtml) {
        this.pathToHtml = pathToHtml;
    }
}
