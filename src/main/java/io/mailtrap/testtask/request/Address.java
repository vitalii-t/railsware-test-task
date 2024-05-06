package io.mailtrap.testtask.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

/**
 * Represents an email address.
 */
@Getter
@Setter
@RequiredArgsConstructor
public class Address extends AbstractModel {

    private String name;

    @Email
    @NotEmpty
    private String email;

}
