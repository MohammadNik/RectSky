package org.RectSky.Exceptions;

public class MatrixNotInvertibleException extends RuntimeException {

    public MatrixNotInvertibleException() {
        super();
    }

    public MatrixNotInvertibleException(String message) {
        super(message);
    }

    public MatrixNotInvertibleException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatrixNotInvertibleException(Throwable cause) {
        super(cause);
    }

    protected MatrixNotInvertibleException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
