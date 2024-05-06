package io.mailtrap.testtask.exception;

/**
 * Custom exception wrapper for JSON serializing/deserializing exceptions
 */
public class JsonException extends BaseMailtrapException {

    public JsonException(String message, Throwable cause) {
        super(message, cause);
    }

}
