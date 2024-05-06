package io.mailtrap.testtask.exception;

/**
 * Custom exception wrapper for request body validation violations
 */
public class InvalidRequestBodyException extends BaseMailtrapException {

    public InvalidRequestBodyException(String message) {
        super(message);
    }

}
