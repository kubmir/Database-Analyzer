package cz.muni.fi.bakalarka1.Utils;

import cz.muni.fi.bakalarka1.Database.ProcessStats;
import cz.muni.fi.bakalarka1.Database.GroupOfLogs;
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
 * Class which writes analyzed data to output XML file. 
 * @author Miroslav Kubus
 */
public class XmlWriter {
    
    private static final Logger LOGGER = Logger.getLogger(XmlWriter.class.getName());
    private final OutputStream outputStream;
    private final XMLStreamWriter writer;
    
    public XmlWriter() throws ServiceFailureException {
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
    
    /**
     * Method which writes start of output xml file. It creates
     * root element called Processes.
     * @throws ServiceFailureException in case of error while writing opening tag 
     */
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
    
    /**
     * Method which writes end of output xml file. It closes
     * root element called Processes.
     * @throws ServiceFailureException in case of error while writing closing tag 
     */
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
    
    /**
     * Method which executes writing to xml file of statistics
     * and logs of specific process name.
     * @param listOfResults represents list of logs to be written
     * @param stats represents statistics about logs of specific 
     * process name obtained in one batch
     * @throws ServiceFailureException in case of error while writing data to 
     * output xml file
     */
    public void writeDataToDoc(List<GroupOfLogs> listOfResults, ProcessStats stats) throws ServiceFailureException {
        try {
            writer.writeStartElement("AppLogs");
            this.writeProcessStats(stats);
  
            for(GroupOfLogs res : listOfResults) {
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
    
    /**
     * Method which write statistics about logs obtained in one batch.
     * @param stats represents statistics about logs of specific 
     * process name obtained in one batch
     * @throws ServiceFailureException in case of error while writing data.
     */
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
    
    /**
     * Method which write one specific group of logs. It writes startID, endID,
     * count, level, pid, tid, type attributes and text node of log.
     * @param res represents one group of logs. Group is created by Analyzer. 
     * @throws ServiceFailureException in case of error while writing data.
     */
    private void writeSpecificResult(GroupOfLogs res) throws ServiceFailureException {
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
