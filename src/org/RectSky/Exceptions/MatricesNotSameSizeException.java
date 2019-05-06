package org.RectSky.Exceptions;

public class MatricesNotSameSizeException extends RuntimeException {

    public MatricesNotSameSizeException() {
        super();
    }

    public MatricesNotSameSizeException(String message) {
        super(message);
    }

    public MatricesNotSameSizeException(String message, Throwable cause) {
        super(message, cause);
    }

    public MatricesNotSameSizeException(Throwable cause) {
        super(cause);
    }

    protected MatricesNotSameSizeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
