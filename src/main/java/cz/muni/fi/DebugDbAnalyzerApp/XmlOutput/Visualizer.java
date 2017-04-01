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

    private final FileWorker fileWorker;
    private final String pathToDbFolder;
    
    public Visualizer(String pathToDB) {
        fileWorker = new FileWorkerImpl();
        pathToDbFolder = fileWorker.getDatabaseFolder(pathToDB);
    }
    
    /**
     * Method which transform xml output to html output using xslt template.
     * @throws ServiceFailureException in case of error while transformation.
     */
    public void toWeb() throws ServiceFailureException {
        String pathToXML = pathToDbFolder + File.separator + "xmlOutput.xml";
        String pathToHtml = "src" + File.separator + "main" + File.separator 
                + "resources" + File.separator + "htmlOutput.html";
        
        String pathToXSLT = "src" + File.separator + "main" + File.separator 
                + "resources" + File.separator + "XSLT.xsl";

        XSLTProcessor pro = new XSLTProcessor();
        pro.transformToHtml(pathToXSLT, pathToXML, pathToHtml);
        pro.openHtml(pathToHtml);
    }
}
