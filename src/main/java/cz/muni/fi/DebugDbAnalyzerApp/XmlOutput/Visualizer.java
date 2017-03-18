package cz.muni.fi.DebugDbAnalyzerApp.XmlOutput;

import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;
import java.io.File;
import java.sql.SQLException;

/**
 * Class which provides operations for transforming xml file
 * to html file and opens html file in default browser of computer.
 * @author Miroslav Kubus
 */
public class Visualizer {
    
    public void toWeb(String pathToDbFolder) throws ServiceFailureException, SQLException {
        String pathToXML = pathToDbFolder + File.separator + "xmlOutput.xml";
        String pathToHtml = "src" + File.separator + "main" + File.separator 
                + "resources" + File.separator + "htmlOutput.html";
        
        String pathToXSLT = "src" + File.separator + "main" + File.separator 
                + "resources" + File.separator + "XSLTsablona.xsl";

        XSLTProcessor pro = new XSLTProcessor();
        pro.transformToHtml(pathToXSLT, pathToXML, pathToHtml);
        pro.openHtml(pathToHtml);
    }
}
