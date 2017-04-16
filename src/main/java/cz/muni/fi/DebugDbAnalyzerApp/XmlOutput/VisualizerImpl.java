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
    
    private final String xsltPath;
    private final String xmlPath;
    private final String htmlPath;
    
    /**
     * Constructor for VisualizerImpl. It sets paths to xml, xslt and html files.
     * @param pathToDataFolder represents path to data folder.
     */
    public VisualizerImpl(String pathToDataFolder) {
        xsltPath = pathToDataFolder + File.separator + "XSLT.xsl";
        xmlPath = pathToDataFolder + File.separator + "xmlOutput.xml";
        htmlPath = pathToDataFolder + File.separator + "htmlOutput.html";
    }
    
    @Override
    public void toWeb(TextAreaLoggerHandler handler) throws ServiceFailureException {        
        XSLTProcessorImpl pro = new XSLTProcessorImpl(handler);
        pro.transformToHtml(xsltPath, xmlPath, htmlPath);
        pro.openFile(htmlPath);
    }
   
    /**
     * Returns path to xml file.
     * @return path to xml file where data are stored.
     */
    public String getXmlPath() {
        return xmlPath;
    }
}
