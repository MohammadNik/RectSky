package org.RectSky.Exceptions;

public class MatrixNotSquareException extends RuntimeException {

    public MatrixNotSquareException() {
        super();
    }

    public MatrixNotSquareException(String message) {
        super(message);
    }

    public MatrixNotSquareException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatrixNotSquareException(Throwable cause) {
        super(cause);
    }

    protected MatrixNotSquareException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
