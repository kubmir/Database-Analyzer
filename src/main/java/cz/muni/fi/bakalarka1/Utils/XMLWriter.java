package cz.muni.fi.bakalarka1.Utils;

import cz.muni.fi.bakalarka1.Database.ProcessStats;
import cz.muni.fi.bakalarka1.Database.Result;
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
import org.w3c.dom.NodeList;

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
    
    public void writeDataToDoc(List<Result> listOfResults, ProcessStats stats) throws ServiceFailureException {
        Element dataRoot = dataDoc.getDocumentElement();
        if(dataRoot != null) {
            NodeList test = dataDoc.getElementsByTagName(stats.getProcessName());
            Element processRoot = (Element) test.item(0);
            
            if(processRoot == null) {
                processRoot = dataDoc.createElement(stats.getProcessName());
            }
                    
            processRoot.setAttribute("error", String.valueOf(stats.getError()));
            processRoot.setAttribute("verbose", String.valueOf(stats.getVerbose()));
            processRoot.setAttribute("info", String.valueOf(stats.getInfo()));
            processRoot.setAttribute("critical", String.valueOf(stats.getCritical()));
            processRoot.setAttribute("debug", String.valueOf(stats.getDebug()));
            processRoot.setAttribute("warning", String.valueOf(stats.getWarning()));
              
            for(Result res : listOfResults) {
                Element resultElement = dataDoc.createElement("LOG");
                resultElement.setAttribute("count",String.valueOf(res.getCount()));
                resultElement.setAttribute("startid", String.valueOf(res.getStartID()));
                resultElement.setAttribute("endid", String.valueOf(res.getEndID()));
                resultElement.setAttribute("level", String.valueOf(res.getLevel()));
                resultElement.appendChild(dataDoc.createTextNode(res.getIdentity()));
                processRoot.appendChild(resultElement);
            }
            
            dataRoot.appendChild(processRoot);
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
