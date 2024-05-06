package io.mailtrap.testtask.response;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Represents a failure response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class FailureResponse extends CommonResponse {
    private List<String> errors;
}
