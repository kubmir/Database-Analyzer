package cz.muni.fi.DebugDbAnalyzerApp.ApplicationUtils;

import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.StreamHandler;
import javax.swing.JTextArea;

public class TextAreaLoggerHandler extends StreamHandler {
    JTextArea textArea = null;

    /**
     * Method which serves for initialization of textAreaHandler.
     * @param textArea 
     */
    public void initTextAreaHandler(JTextArea textArea) {
        this.textArea = textArea;
        this.setLevel(Level.ALL);
        this.setFormatter(new TextAreaLoggerFormatter());
    }

    @Override
    public void publish(LogRecord record) {
        super.publish(record);
        flush();

        if (textArea != null) {
            textArea.append(getFormatter().format(record));
            textArea.setCaretPosition(textArea.getDocument().getLength());
        }
    }
}