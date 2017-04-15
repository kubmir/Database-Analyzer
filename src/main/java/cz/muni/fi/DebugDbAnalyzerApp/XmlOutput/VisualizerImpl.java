package cz.muni.fi.DebugDbAnalyzerApp.XmlOutput;

import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;
import cz.muni.fi.DebugDbAnalyzerApp.ApplicationUtils.TextAreaLoggerHandler;
import java.io.File;

/**
 * Class which provides operations for transforming xml file
 * to html file and opens html file in default browser of computer.
 * @author Miroslav Kubus
 */
public class VisualizerImpl implements Visualizer {
    private static final String PROJECT_FOLDER = "src" + File.separator 
            + "main" + File.separator + "resources";
    
    private static final String PATH_TO_XSLT = PROJECT_FOLDER
            + File.separator + "XSLT.xsl";
    
    private static final String PATH_TO_XML = PROJECT_FOLDER 
                + File.separator + "xmlOutput.xml";
    
    private static final String PATH_TO_HTML = PROJECT_FOLDER 
                + File.separator + "htmlOutput.html";
    
    
    @Override
    public void toWeb(TextAreaLoggerHandler handler) throws ServiceFailureException {        
        XSLTProcessorImpl pro = new XSLTProcessorImpl(handler);
        pro.transformToHtml(PATH_TO_XSLT, PATH_TO_XML, PATH_TO_HTML);
        pro.openFile(PATH_TO_HTML);
    }
   
    /**
     * Returns path to xml file.
     * @return path to xml file where data are stored.
     */
    public static String getPATH_TO_XML() {
        return PATH_TO_XML;
    }
}
