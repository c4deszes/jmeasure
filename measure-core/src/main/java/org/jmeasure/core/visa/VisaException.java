package org.jmeasure.core.visa;

/**
 * VisaException
 */
public class VisaException extends Exception {

    public VisaException() {
    }

    public VisaException(String message) {
        super(message);
    }

    public VisaException(Throwable cause) {
        super(cause);
    }

    public VisaException(String message, Throwable cause) {
        super(message, cause);
    }

    public VisaException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    
}