package org.RectSky.Exceptions;

public class MatrixFullException extends RuntimeException {

    public MatrixFullException() {
        super();
    }

    public MatrixFullException(String message) {
        super(message);
    }

    public MatrixFullException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatrixFullException(Throwable cause) {
        super(cause);
    }

    protected MatrixFullException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
