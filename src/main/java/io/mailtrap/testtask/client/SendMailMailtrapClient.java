package io.mailtrap.testtask.client;

import io.mailtrap.testtask.Mapper;
import io.mailtrap.testtask.exception.BaseMailtrapException;
import io.mailtrap.testtask.exception.InvalidRequestBodyException;
import io.mailtrap.testtask.exception.JsonException;
import io.mailtrap.testtask.request.CommonMail;
import io.mailtrap.testtask.response.CommonResponse;
import io.mailtrap.testtask.response.FailureResponse;
import io.mailtrap.testtask.response.SuccessResponse;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import lombok.Setter;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.stream.Collectors;

import static io.mailtrap.testtask.Constants.*;

/**
 * Mailtrap client implementation for sending emails.
 */
public class SendMailMailtrapClient implements MailtrapClient {

    @Setter
    private boolean sandboxEnvironment = true;

    private final String token;
    private final int inboxId;

    /**
     * Setter for case when client would like to customize httpClient - set timeout, follow redirects, etc
     */
    @Setter
    private HttpClient httpClient;
    private final Validator validator;

    /**
     * Constructs a new Client.
     *
     * @param token   the authentication token for accessing the Mailtrap API
     * @param inboxId the ID of the inbox to which emails will be sent. Required to use in sandbox environment
     */
    public SendMailMailtrapClient(String token, int inboxId) {
        this.token = token;
        this.inboxId = inboxId;
        this.httpClient = HttpClient.newBuilder().build();

        // Wrapped into try-with-resources to ensure that factory's resources are properly closed
        try (ValidatorFactory factory = Validation.buildDefaultValidatorFactory()) {
            this.validator = factory.getValidator();
        }
    }

    /**
     * Sends an email using Mailtrap API.
     *
     * @param mail the email to be sent
     * @return the response from the Mailtrap API
     * @throws InvalidRequestBodyException if the request object is invalid
     * @throws BaseMailtrapException       if an error occurs while sending the API request
     */
    @Override
    public CommonResponse send(CommonMail mail) {
        validateRequestBodyOrThrowException(mail);

        var uri = buildURI();
        var request = buildRequest(mail, uri);
        try {
            var response = httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
            return handleResponse(response);
        } catch (InterruptedException | IOException e) {
            throw new BaseMailtrapException("An error has occurred while sending request", e);
        }
    }

    /**
     * Validates the request body before sending a request to Mailtrap API.
     *
     * @param mail request body
     * @throws InvalidRequestBodyException if there are violations error validating the body
     */
    private void validateRequestBodyOrThrowException(CommonMail mail) {
        if (mail == null) {
            throw new InvalidRequestBodyException("Mail must not be null");
        }

        if ((mail.getText() == null || mail.getText().isEmpty()) && (mail.getHtml() == null || mail.getHtml().isEmpty())) {
            throw new InvalidRequestBodyException("Mail text or html or both must not be null or empty");
        }

        String violations = validator.validate(mail).stream()
                .map(violation -> violation.getPropertyPath().toString() + "=" + violation.getMessage())
                .collect(Collectors.joining("; "));

        if (!violations.isEmpty()) {
            throw new InvalidRequestBodyException("Invalid request body. Violations: " + violations);
        }
    }

    /**
     * Handles the HTTP response from Mailtrap API.
     *
     * @param response the HTTP response
     * @return the parsed response object
     * @throws BaseMailtrapException if response body is null
     * @throws JsonException         if an error occurs while parsing the response
     */
    private CommonResponse handleResponse(HttpResponse<InputStream> response) {
        try (InputStream body = response.body()) {
            if (body == null) {
                throw new BaseMailtrapException("Response body is null");
            }

            if (response.statusCode() == 200) {
                return Mapper.get().readValue(body, SuccessResponse.class);
            } else {
                return Mapper.get().readValue(body, FailureResponse.class);
            }
        } catch (IOException e) {
            throw new JsonException("An error has occurred while converting JSON", e);
        }
    }

    /**
     * Builds the HTTP request (headers, maps body, etc.) for sending the email.
     *
     * @param mail request body
     * @param uri  the URI of the Mailtrap API send endpoint
     * @return the HTTP request
     */
    private HttpRequest buildRequest(CommonMail mail, URI uri) {
        return HttpRequest.newBuilder()
                .uri(uri)
                .header("Accept", "application/json")
                .header("Content-Type", "application/json; charset=UTF-8")
                .header("Authorization", "Bearer " + token)
                .POST(HttpRequest.BodyPublishers.ofString(mail.toJson()))
                .build();
    }

    /**
     * Builds the URI for the Mailtrap API endpoint.
     * In case {@link #sandboxEnvironment} is true - required to have {@link #inboxId} set.
     *
     * @return generated URI of the Mailtrap API endpoint
     * @throws BaseMailtrapException if an error occurs while creating the URI
     */
    private URI buildURI() {
        try {
            var url = sandboxEnvironment ? SANDBOX_URL + SEND_ENDPOINT + "/" + inboxId
                    : PRODUCTION_URL + SEND_ENDPOINT;
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new BaseMailtrapException("An error has occurred while creating URL", e);
        }
    }

}