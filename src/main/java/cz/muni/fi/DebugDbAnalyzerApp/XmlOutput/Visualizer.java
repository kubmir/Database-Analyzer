package cz.muni.fi.DebugDbAnalyzerApp.XmlOutput;

import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;
import java.io.File;

/**
 * Class which provides operations for transforming xml file
 * to html file and opens html file in default browser of computer.
 * @author Miroslav Kubus
 */
public class Visualizer {
    private static final String PROJECT_FOLDER = "src" + File.separator 
            + "main" + File.separator + "resources";
    
    private static final String PATH_TO_XSLT = PROJECT_FOLDER
            + File.separator + "XSLT.xsl";
    
    private static final String PATH_TO_XML = PROJECT_FOLDER 
                + File.separator + "xmlOutput.xml";
    
    private static final String PATH_TO_HTML = PROJECT_FOLDER 
                + File.separator + "htmlOutput.html";
    
    /**
     * Method which transform xml output to html output using xslt template.
     * @throws ServiceFailureException in case of error while transformation.
     */
    public void toWeb() throws ServiceFailureException {        
        XSLTProcessor pro = new XSLTProcessor();
        pro.transformToHtml(PATH_TO_XSLT, PATH_TO_XML, PATH_TO_HTML);
        pro.openFile(PATH_TO_HTML);
    }
    
    public static String getPATH_TO_XML() {
        return PATH_TO_XML;
    }
}
