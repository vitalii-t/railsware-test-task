package io.mailtrap.testtask.exception;

/**
 * Base custom exception. Should be parent exception for all other custom exceptions
 */
public class BaseMailtrapException extends RuntimeException {

    public BaseMailtrapException(final String message) {
        super(message);
    }

    public BaseMailtrapException(final String message, final Throwable cause) {
        super(message, cause);
    }

}
