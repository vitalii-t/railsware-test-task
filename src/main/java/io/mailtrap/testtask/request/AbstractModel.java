package io.mailtrap.testtask.request;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.mailtrap.testtask.Mapper;
import io.mailtrap.testtask.exception.JsonException;

/**
 * Abstract class representing a model with common functionality for serialization.
 */
public abstract class AbstractModel {
    /**
     * Converts the object to JSON.
     *
     * @return JSON representation of the object
     */
    public final String toJson() {
        try {
            return Mapper.get().writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new JsonException("An error has occurred while serializing the object to JSON", e);
        }
    }

    @Override
    public String toString() {
        return getClass().getName() + " [ " + toJson() + " ]";
    }
}
