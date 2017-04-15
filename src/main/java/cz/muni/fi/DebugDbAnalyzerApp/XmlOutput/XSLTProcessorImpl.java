package cz.muni.fi.DebugDbAnalyzerApp.XmlOutput;

import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;
import cz.muni.fi.DebugDbAnalyzerApp.ApplicationUtils.TextAreaLoggerHandler;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 * Class which execute transformation of xml to html 
 * and opening of html file in default browser. It implements XSLTProcessor.
 * @author Miroslav Kubus
 */
public class XSLTProcessorImpl implements XSLTProcessor {
    
    private static final Logger LOGGER = Logger.getLogger(XSLTProcessorImpl.class.getName());

    /**
     * Constructor for XLSTProcessor. It registers handler for logger.
     * @param handler represents handler for visualizing errors in frontend
     */
    public XSLTProcessorImpl(TextAreaLoggerHandler handler) {
        if(LOGGER.getHandlers().length == 0) {
            LOGGER.addHandler(handler);
        }
    }
    
    @Override
    public void transformToHtml(String xsltPath, String xmlPath, String htmlPath) throws ServiceFailureException {
        LOGGER.log(Level.INFO, "Executing transformation to HTML ...");
        
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer xsltProc = tf.newTransformer(new StreamSource(new File(xsltPath)));
            
            xsltProc.transform(new StreamSource(new File(xmlPath)), 
                    new StreamResult(new File(htmlPath)));
            
            LOGGER.log(Level.INFO, "Transformation to HTML finished!");
        } catch(TransformerConfigurationException ex) {
            LOGGER.log(Level.SEVERE, "Error while configuring transformer!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "configuring transformer!", ex);
        } catch(TransformerException ex) {
            LOGGER.log(Level.SEVERE, "Error while transformation XML to HTML!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "transformation!", ex);
        }   
    }

    @Override
    public void openFile(String filePath) throws ServiceFailureException {
        LOGGER.log(Level.INFO, "Opening file {0} ...", filePath);
        try {
            File htmlFile = new File(filePath);
            Desktop.getDesktop().browse(htmlFile.toURI());
            LOGGER.log(Level.INFO, "File {0} opened!", filePath);
        } catch(IOException ex) {
            LOGGER.log(Level.SEVERE, "Error while opening html file "
                    + "in default browser!", ex);
            throw new ServiceFailureException("Internal error: error while opening "
                    + "html file in default web browser!", ex);
        }
    }
}