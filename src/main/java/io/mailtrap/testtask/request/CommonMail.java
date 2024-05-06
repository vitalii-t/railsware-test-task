package io.mailtrap.testtask.request;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * Represents a common email.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class CommonMail extends AbstractModel {

    @NotNull
    @Valid
    private Address from;

    @NotEmpty
    @Valid
    private List<Address> to;

    @NotEmpty
    private String subject;

    private String text;

    private String html;

    @Valid
    private List<Attachment> attachments;

}
