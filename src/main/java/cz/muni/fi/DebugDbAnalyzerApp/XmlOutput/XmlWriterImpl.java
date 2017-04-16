package cz.muni.fi.DebugDbAnalyzerApp.XmlOutput;

import cz.muni.fi.DebugDbAnalyzerApp.DataStorage.ErrorAndCriticalStats;
import cz.muni.fi.DebugDbAnalyzerApp.DataStorage.GroupOfLogs;
import cz.muni.fi.DebugDbAnalyzerApp.DataStorage.ProcessStats;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;
import cz.muni.fi.DebugDbAnalyzerApp.ApplicationUtils.TextAreaLoggerHandler;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.FileWorkerImpl;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

/**
 * Class which writes analyzed data and statistics to output XML file. 
 * It implements XmlWriter interface.
 * @author Miroslav Kubus
 */
public class XmlWriterImpl implements XmlWriter {
    
    private static final Logger LOGGER = Logger.getLogger(XmlWriterImpl.class.getName());
    private final OutputStream outputStream;
    private final XMLStreamWriter writer;
    
    /**
     * Constructor which creates new file xmlOutput.xml in database folder.
     * @param pathToDataFolder represents path to folder of application data.
     * @param textAreaHandler represents handler for visualizing errors in frontend
     * @throws ServiceFailureException in case of error during creating writer
     * and output stream.
     */
    public XmlWriterImpl(String pathToDataFolder, TextAreaLoggerHandler textAreaHandler) throws ServiceFailureException {
        try {            
            outputStream = new FileOutputStream(new File(pathToDataFolder
                + File.separator + "xmlOutput.xml"));
            
            writer = XMLOutputFactory.newInstance().createXMLStreamWriter(
                new OutputStreamWriter(outputStream, "utf-8"));
            
            if(LOGGER.getHandlers().length == 0) {
                LOGGER.addHandler(textAreaHandler);
            }
        } catch(FileNotFoundException | UnsupportedEncodingException | XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error while creating XML writer!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "creating XML writer!", ex);
        }
    }
    

    @Override
    public void writeStartOfDocument() throws ServiceFailureException {
        try {
            writer.writeStartDocument();
            this.writeStartOfElement("Database");
        } catch (XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error while writing start of XML document!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "writing start of XML document!", ex);
        }
    }
    
    @Override
    public void writeEndOfDocument() throws ServiceFailureException {
        try {
            this.writeEndOfElement();
            writer.writeEndDocument();
            writer.close();
        } catch (XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error while writing end of XML document!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "writing end of XML document!", ex);
        }
    }
    
    @Override
    public void writeStartOfElement(String elementName) throws ServiceFailureException {
         try {
            writer.writeStartElement(elementName);
        } catch (XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error while writing start of XML element "
                    + "with name " + elementName + "!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "writing start of XML element " + elementName + "!", ex);
        }
    }
    
    @Override
    public void writeEndOfElement() throws ServiceFailureException {
        try {
            writer.writeEndElement();
        } catch (XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error while writing end of XML element!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "writing end of XML element!", ex);
        } 
    }
    
    @Override
    public void writeLogsToOutputDoc(List<GroupOfLogs> listOfResults, ProcessStats stats) throws ServiceFailureException {
        try {
            writeStartOfElement("AppLogs");
            writeProcessStats(stats);
  
            for(GroupOfLogs res : listOfResults) {
                writeSpecificResult(res);
            }
            
            writeEndOfElement();
            writer.flush();
        } catch (XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error while writing logs to XML document!", ex);
            throw new ServiceFailureException("Internal error: error while "
                    + "writing logs to XML document!", ex);
        }
    }
    
    @Override
    public void writeDatabaseStats(Map<String, ErrorAndCriticalStats> stats,
            String elementName) throws ServiceFailureException {
        
        writeStartOfElement(elementName);
        
        try {
            writer.writeAttribute("count", String.valueOf(stats.size()));
        } catch (XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error while writing element " 
                    + elementName + "!", ex);
            throw new ServiceFailureException("Error while writing"
                    + " element " + elementName + "!", ex);
        }
        
        String el = elementName.substring(0, elementName.length() - 5); 
        for(String key : stats.keySet()) {
            writeStatistics(key, stats.get(key), el);
        }
        
        writeEndOfElement();
    }
    
    /**
     * Method which write statistics about database in form of element Statistic 
     * which contains elements name, errors and criticals with its values.
     * @param key represents value of element name
     * @param stats represents values of elements errors and criticals
     * @throws ServiceFailureException in case of error while writing statistics 
     * to output xml file.
     */
    private void writeStatistics(String key, ErrorAndCriticalStats stats, String element) throws ServiceFailureException {
        writeStartOfElement(element);       
        writeElementWithCharacters("Name", key);
        writeElementWithCharacters("Errors", String.valueOf(stats.getError()));
        writeElementWithCharacters("Criticals", String.valueOf(stats.getCritical()));
        writeEndOfElement();
    }
    
    /**
     * Method which writes element with value to output xml file.
     * @param name represents name of element.
     * @param value represents value of element.
     * @throws ServiceFailureException in case of error while 
     * writing element to output xml file.
     */
    private void writeElementWithCharacters(String name, String value) throws ServiceFailureException {
        try {
            writer.writeStartElement(name);
            writer.writeCharacters(value);
            writer.writeEndElement();
        } catch (XMLStreamException ex) {
            LOGGER.log(Level.SEVERE, "Error while writing element " 
                    + name + "!", ex);
            throw new ServiceFailureException("Error while writing"
                    + " element " + name + "!", ex);
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
        writeStartOfElement("Log");
        
        if(res.getType().compareTo("Error") == 0 || res.getType().compareTo("Critical") == 0) {
            writeElementWithCharacters("info", removeNonValidXMLCharacters(res.getIdentity()));
        } else {
            writeElementWithCharacters("info", String.valueOf(res.getIdentity()));
        }
        
        writeElementWithCharacters("startID", String.valueOf(res.getStartID()));
        writeElementWithCharacters("endID", String.valueOf(res.getEndID()));
        writeElementWithCharacters("startDate", res.getStartDate());
        writeElementWithCharacters("endDate", res.getEndDate());
        writeElementWithCharacters("module", String.valueOf(res.getModule()));
        writeElementWithCharacters("level", String.valueOf(res.getLevel()));
        writeElementWithCharacters("type", res.getType());
        writeElementWithCharacters("tid", String.valueOf(res.getThreadID()));
        writeElementWithCharacters("pid", String.valueOf(res.getProcessID()));
        writeElementWithCharacters("count", String.valueOf(res.getCount()));
        writeEndOfElement();
    }
    
    /**
     * Method which remove non valid xml characters from description of errors
     * @param description string to be checked
     * @return string same as description without forbidden characters
     */
    private String removeNonValidXMLCharacters(String description) {
        StringBuilder output = new StringBuilder();
        char current;

        if (description == null || description.compareTo("") == 0) {
            return "";
        }
        
        for (int i = 0; i < description.length(); i++) {
            current = description.charAt(i);
            
            if ((current == 0x9) || (current == 0xA) || (current == 0xD) ||
                ((current >= 0x20) && (current <= 0xD7FF)) ||
                ((current >= 0xE000) && (current <= 0xFFFD)) ||
                ((current >= 0x10000) && (current <= 0x10FFFF))) {
                
                output.append(current);
            } else {
                output.append(' ');
            }
        }
        
        return output.toString();
    }
}