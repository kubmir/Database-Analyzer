package cz.muni.fi.DebugDbAnalyzerApp.XmlOutput;

import cz.muni.fi.DebugDbAnalyzerApp.Utils.ErrorAndCriticalStats;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.GroupOfLogs;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.ProcessStats;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;
import java.util.List;
import java.util.Map;

/**
 * Interface which defines methods for xml writer.
 * @author Miroslav Kubus
 */
public interface XmlWriter {
    
    /**
     * Method which writes start of output xml file. It creates
     * root element called Processes.
     * @throws ServiceFailureException in case of error while writing opening tag 
     */
    public void writeStartOfDocument() throws ServiceFailureException;
    
    /**
     * Method which writes end of output xml file. It closes
     * root element called Processes.
     * @throws ServiceFailureException in case of error while writing closing tag 
     */
    public void writeEndOfDocument() throws ServiceFailureException;
    
    /**
     * Method which writes start of element with name elementName.
     * @param elementName name of element to be written
     * @throws ServiceFailureException in case of error while writing start of element
     */
    public void writeStartOfElement(String elementName) throws ServiceFailureException;
    
    /**
     * Method which writes end of current element.
     * @throws ServiceFailureException in case of error while writing end of element
     */
    public void writeEndOfElement() throws ServiceFailureException;
    
    /**
     * Method which executes writing to xml file of statistics
     * and logs of specific process name.
     * @param listOfResults represents list of logs to be written
     * @param stats represents statistics about logs of specific 
     * process name obtained in one batch
     * @throws ServiceFailureException in case of error while writing data to 
     * output xml file
     */
    public void writeLogsToOutputDoc(List<GroupOfLogs> listOfResults, ProcessStats stats) throws ServiceFailureException;
    
    /**
     * Method which writes database statistics to output xml file.
     * @param stats represent map which stores statistics about database
     * @param elementName represent name of element to be created
     * @throws ServiceFailureException in case of error while writing 
     * database statistics to output file.
     */
    public void writeDatabaseStats(Map<String, ErrorAndCriticalStats> stats,
        String elementName) throws ServiceFailureException;
}
