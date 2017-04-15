package cz.muni.fi.DebugDbAnalyzerApp.XmlOutput;

import cz.muni.fi.DebugDbAnalyzerApp.ApplicationUtils.TextAreaLoggerHandler;
import cz.muni.fi.DebugDbAnalyzerApp.Utils.ServiceFailureException;

/**
 * Interface Visualizer defines method which visualize xml data.
 * @author Miroslav Kubus
 */
public interface Visualizer {
    
    /**
     * Method which transform xml output to html output using xslt template.
     * @param handler represents handler for XSLTProcessorImpl.
     * @throws ServiceFailureException in case of error while transformation.
     */
    public void toWeb(TextAreaLoggerHandler handler) throws ServiceFailureException;
}
