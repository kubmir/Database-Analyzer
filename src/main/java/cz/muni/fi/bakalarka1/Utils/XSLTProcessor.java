package cz.muni.fi.bakalarka1.Utils;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

/**
 *
 * @author Miroslav Kubus
 */
public class XSLTProcessor {
    
    public void transformToHtml(String xsltPath, String drboPath, String htmlPath) throws ServiceFailureException {
        try {
            TransformerFactory tf = TransformerFactory.newInstance();
            Transformer xsltProc = tf.newTransformer(new StreamSource(new File(xsltPath)));
            
            xsltProc.transform(new StreamSource(new File(drboPath)), new StreamResult(new File(htmlPath)));
            
        } catch(TransformerConfigurationException ex) {
            throw new ServiceFailureException("Internal error: error while "
                    + "configuring transformer!", ex);
        } catch(TransformerException ex) {
            throw new ServiceFailureException("Internal error: error while "
                    + "transformation!", ex);
        }   
    }
    
    public void openHtml(String htmlPath) throws ServiceFailureException {
        try {
            File htmlFile = new File(htmlPath);
            Desktop.getDesktop().browse(htmlFile.toURI());
        } catch(IOException ex) {
            throw new ServiceFailureException("Internal error: error while opening "
                    + "html file in default web browser!", ex);
        }
    }
    
}