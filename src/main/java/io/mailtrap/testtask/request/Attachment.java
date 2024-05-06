package io.mailtrap.testtask.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents an attachment in an email.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Attachment extends AbstractModel {

    @NotEmpty
    private String content;

    private String type;

    @NotEmpty
    private String filename;

    private String disposition;

    @JsonProperty("content_id")
    private String contentId;

}
