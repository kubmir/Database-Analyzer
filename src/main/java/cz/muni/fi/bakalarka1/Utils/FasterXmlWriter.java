package cz.muni.fi.bakalarka1.Utils;

import cz.muni.fi.bakalarka1.Database.ProcessStats;
import cz.muni.fi.bakalarka1.Database.Result;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Class which writes data to output XML file. 
 * @author Miroslav Kubus
 */
public class FasterXmlWriter {
    
    private static final Logger LOGGER = Logger.getLogger(FasterXmlWriter.class.getName());
    private final OutputStream outputStream;
    private final XMLStreamWriter writer;
    
    public FasterXmlWriter() throws ServiceFailureException {
        try {
            outputStream = new FileOutputStream(new File("C:\\Users\\Miroslav Kubus\\Desktop\\Nepodporovany port v emailovem klientu\\myXmlTest.xml"));
            writer = XMLOutputFactory.newInstance().createXMLStreamWriter(
                new OutputStreamWriter(outputStream, "utf-8"));
        } catch(FileNotFoundException | UnsupportedEncodingException | XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error while creating XML writer!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "creating XML writer!", ex);
        }
    }
    
    public void writeStartOfDocument() throws ServiceFailureException {
        try {
            writer.writeStartDocument();
            writer.writeStartElement("Processes");
        } catch (XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error while writing start of XML document!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "writing start of XML document!", ex);
        }
    }
    
    public void writeEndOfDocument() throws ServiceFailureException {
        try {
            writer.writeEndElement();
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error whiel writing end of XML document!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "writing end of XML document!", ex);
        }
    }
    
    public void writeDataToDoc(List<Result> listOfResults, ProcessStats stats) throws ServiceFailureException {
        try {
            writer.writeStartElement("AppLogs");
            this.writeProcessStats(stats);
  
            for(Result res : listOfResults) {
                this.writeSpecificResult(res);
            }
            
            writer.writeEndElement();
            writer.flush();
        } catch (XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error while writing logs to XML document!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "writing logs to XML document!", ex);
        }
    }
    
    private void writeProcessStats(ProcessStats stats) throws ServiceFailureException {
        try {
            writer.writeAttribute("processName", stats.getProcessName());
            writer.writeAttribute("error", String.valueOf(stats.getError()));
            writer.writeAttribute("debug", String.valueOf(stats.getDebug()));
            writer.writeAttribute("critical", String.valueOf(stats.getCritical()));
            writer.writeAttribute("info", String.valueOf(stats.getInfo()));
            writer.writeAttribute("warning", String.valueOf(stats.getWarning()));
            writer.writeAttribute("verbose", String.valueOf(stats.getVerbose()));
        } catch (XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error while writing process statistics - " 
                    + stats.getProcessName() + "!", ex);
            throw new ServiceFailureException("Error while writing process statistics - " 
                    + stats.getProcessName() + "!", ex);
        }
    }
    
    private void writeSpecificResult(Result res) throws ServiceFailureException {
        try {
            writer.writeStartElement("Log");
            writer.writeAttribute("startID", String.valueOf(res.getStartID()));
            writer.writeAttribute("endID", String.valueOf(res.getEndID()));
            writer.writeAttribute("count", String.valueOf(res.getCount()));
            writer.writeAttribute("level", String.valueOf(res.getLevel()));
            writer.writeAttribute("pid", String.valueOf(res.getProcessID()));
            writer.writeAttribute("tid", String.valueOf(res.getThreadID()));
            writer.writeAttribute("type", res.getType());
            writer.writeCharacters(res.getIdentity());
            writer.writeEndElement();
        } catch (XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error while writing log with startID " 
                    + res.getStartID() + "!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "writing log with startID " + res.getStartID() + "!", ex);
        }
    }
}
