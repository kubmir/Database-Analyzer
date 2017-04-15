package cz.muni.fi.DebugDbAnalyzerApp.XmlOutput;

import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;

/**
 * Interface XSLTProcessor defines methods used for transformation xml to html
 * and opening file in default browser.
 * @author Miroslav Kubus
 */
public interface XSLTProcessor {
    
    /**
     * Method which process transformation of xml file to html file.
     * @param xsltPath represents path to xslt template used for transformation
     * @param xmlPath represents path to source xml file
     * @param htmlPath represents path to output html file
     * @throws ServiceFailureException in case of error while transformation process
     */
    public void transformToHtml(String xsltPath, String xmlPath, String htmlPath) 
            throws ServiceFailureException;
    
    /**
     * Method which open output html file in default browser of computer.
     * @param filePath represents path to output file
     * @throws ServiceFailureException in case of error during opening html file.
     */
    public void openFile(String filePath) throws ServiceFailureException;
}
