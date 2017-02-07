package cz.muni.fi.bakalarka1.Utils;

/**
 * Class which represents exception which is thrown in case of 
 * any internal system failure.
 * @author Miroslav Kubus
 */
public class ServiceFailureException extends Exception {

    /**
     * Creates a new instance of ServiceFailureException without
     * detail message.
     */
    public ServiceFailureException() {
    }

    /**
     * Constructs an instance of ServiceFailureException with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public ServiceFailureException(String msg) {
        super(msg);
    }
    
    /**
     * Constructs an instance of ServiceFailureException with the
     * specified throwable which cause ServiceFailureException
     *
     * @param cause the reason why ServiceFailureException is thrown
     */
    public ServiceFailureException(Throwable cause) {
        super(cause);
    }
    
    /**
     * Constructs an instance of ServiceFailureException with the
     * specified throwable which cause ServiceFailureException and specified 
     * detail message
     *
     * @param msg the detail message.
     * @param cause the reason why ServiceFailureException is thrown
     */
    public ServiceFailureException(String msg, Throwable cause) {
        super(msg,cause);
    }
}
