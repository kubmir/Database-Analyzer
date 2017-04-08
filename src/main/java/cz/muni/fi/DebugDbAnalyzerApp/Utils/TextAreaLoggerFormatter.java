package cz.muni.fi.DebugDbAnalyzerApp.Utils;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.LogRecord;
import java.util.logging.SimpleFormatter;

/**
 * Class which serves as formatter for logger output in frontend.
 * @author Miroslav Kubus
 */
public class TextAreaLoggerFormatter extends SimpleFormatter { 

    private static final DateFormat DF = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");

    @Override
    public String format(LogRecord record) {
        StringBuilder builder = new StringBuilder(1000);
        String sourceClass = record.getSourceClassName();
        String className = sourceClass.substring(sourceClass.lastIndexOf(".") + 1, sourceClass.length());
        
        builder.append(DF.format(new Date(record.getMillis()))).append(" - ");
        builder.append("[").append(className).append(".");
        builder.append(record.getSourceMethodName()).append("]");
        builder.append("\n");
        builder.append("[").append(record.getLevel()).append("] -> ");
        builder.append(formatMessage(record));
        builder.append("\n");
        
        return builder.toString();
    }
}