package cz.muni.fi.DebugDbAnalyzerApp.XmlOutput;

import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;
import java.io.File;

/**
 * Class which provides operations for transforming xml file
 * to html file and opens html file in default browser of computer.
 * @author Miroslav Kubus
 */
public class Visualizer {
    
    /**
     * Method which transform xml output to html output using xslt template.
     * @param pathToDbFolder path to folder of database.
     * @throws ServiceFailureException in case of error while transformation.
     */
    public void toWeb(String pathToDbFolder) throws ServiceFailureException {
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
