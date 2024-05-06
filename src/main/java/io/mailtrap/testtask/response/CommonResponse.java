package io.mailtrap.testtask.response;

import io.mailtrap.testtask.request.AbstractModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * Base response class for API calls.
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommonResponse extends AbstractModel {

    private boolean success;

}
