package cz.muni.fi.bakalarka1.Utils;

import cz.muni.fi.bakalarka1.Database.Result;
import java.io.IOException;
import java.net.URI;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 *
 * @author Miroslav Kubus
 */
public class XMLWriter {
    
    private Document dataDoc;
   
    public void createDataDoc() throws ServiceFailureException {
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            dataDoc = builder.newDocument();
            dataDoc.appendChild(dataDoc.createElement("DATALOGS"));
            this.serializetoXML();
        } catch (ParserConfigurationException ex) {
            throw new ServiceFailureException("Internal error: error while "
                    + "configuration of parser!", ex);
        }
    }
    
    public void writeDataToDoc(List<Result> listOfResults, String process) throws ServiceFailureException {
        Element dataRoot = dataDoc.getDocumentElement();
        if(dataRoot != null) {
            Element processRoot = dataDoc.createElement(process);
            
            if(processRoot != null) {
                for(Result res : listOfResults) {
                    Element resultElement = dataDoc.createElement("LOG");
                    resultElement.appendChild(dataDoc.createTextNode(res.getInfo()));
                    //resultElement.appendChild(dataDoc.createTextNode(res.getLog()));
                    processRoot.appendChild(resultElement);
                }
                dataRoot.appendChild(processRoot);
            }
        }
        this.serializetoXML();
    }
    
     public void serializetoXML() throws ServiceFailureException {
        try {
            String output = "C:\\Users\\Miroslav Kubus\\Desktop\\Nepodporovany port v emailovem klientu\\test.xml";
            // Vytvorime instanci tovarni tridy
            TransformerFactory factory = TransformerFactory.newInstance();
            // Pomoci tovarni tridy ziskame instanci tzv. kopirovaciho transformeru
            Transformer transformer = factory.newTransformer();
            // Vstupem transformace bude dokument v pameti
            DOMSource source = new DOMSource(dataDoc);
            // Vystupem transformace bude vystupni soubor
            StreamResult result = new StreamResult(output);
            // Provedeme transformaci
            transformer.transform(source, result);
        } catch(TransformerConfigurationException ex) {
            
        } catch(TransformerException ex) {
            
        }
    }

    
}
