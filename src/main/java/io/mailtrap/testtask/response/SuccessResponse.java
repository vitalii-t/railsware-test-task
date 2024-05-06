package io.mailtrap.testtask.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * Represents a success response.
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SuccessResponse extends CommonResponse {
    @JsonProperty("message_ids")
    private List<String> messageIds;
}
