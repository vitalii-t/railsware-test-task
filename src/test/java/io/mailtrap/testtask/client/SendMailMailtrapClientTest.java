package io.mailtrap.testtask.client;

import io.mailtrap.testtask.exception.BaseMailtrapException;
import io.mailtrap.testtask.exception.InvalidRequestBodyException;
import io.mailtrap.testtask.exception.JsonException;
import io.mailtrap.testtask.request.Address;
import io.mailtrap.testtask.request.Attachment;
import io.mailtrap.testtask.request.CommonMail;
import io.mailtrap.testtask.response.CommonResponse;
import io.mailtrap.testtask.response.FailureResponse;
import io.mailtrap.testtask.response.SuccessResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

public class SendMailMailtrapClientTest {

    private SendMailMailtrapClient mailClient;
    private HttpClient mockHttpClient;

    @BeforeEach
    void setUp() {
        mockHttpClient = Mockito.mock(HttpClient.class);
        mailClient = new SendMailMailtrapClient("dummyToken", 12345);
        mailClient.setHttpClient(mockHttpClient);
    }

    @Test
    void send_InvalidMail_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        CommonMail mail = createTestMail();
        mail.getFrom().setEmail("");

        // Assert
        assertThrows(InvalidRequestBodyException.class, () -> mailClient.send(mail));
    }

    @Test
    void send_NullableMail_ThrowsInvalidRequestBodyException() {
        // Assert
        assertThrows(InvalidRequestBodyException.class, () -> mailClient.send(null));
    }

    @Test
    void send_BothTextAndHtmlAreNullInvalidMail_ThrowsInvalidRequestBodyException() {
        // Set up invalid data
        CommonMail mail = createTestMail();
        mail.setText(null);
        mail.setHtml(null);

        // Assert
        assertThrows(InvalidRequestBodyException.class, () -> mailClient.send(mail));
    }

    @Test
    void send_ValidMail_SuccessResponse() throws IOException, InterruptedException {
        // Set up test data
        CommonMail mail = createTestMail();

        // Set up mocks
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        InputStream inputStream = new ByteArrayInputStream("{\"success\": true, \"message_ids\": [\"sample_message_id\"]}".getBytes());
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(inputStream);
        when(mockHttpClient.send(Mockito.any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofInputStream())))
                .thenReturn(mockResponse);

        // Perform call
        CommonResponse response = mailClient.send(mail);

        // Assert
        assertInstanceOf(SuccessResponse.class, response);
        assertTrue(response.isSuccess());
        assertFalse(((SuccessResponse) response).getMessageIds().isEmpty());
        assertEquals("sample_message_id", ((SuccessResponse) response).getMessageIds().getFirst());
    }

    @Test
    void send_ValidMailWithIncorrectAuth_FailureResponse() throws IOException, InterruptedException {
        // Set up test data
        CommonMail mail = createTestMail();

        // Set up mocks
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        InputStream inputStream = new ByteArrayInputStream("{\"success\": false, \"errors\": [\"Unauthorized\"]}".getBytes());
        when(mockResponse.statusCode()).thenReturn(401);
        when(mockResponse.body()).thenReturn(inputStream);
        when(mockHttpClient.send(Mockito.any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofInputStream())))
                .thenReturn(mockResponse);

        // Perform call
        CommonResponse response = mailClient.send(mail);

        // Assert
        assertInstanceOf(FailureResponse.class, response);
        assertFalse(response.isSuccess());
        assertFalse(((FailureResponse) response).getErrors().isEmpty());
        assertEquals("Unauthorized", ((FailureResponse) response).getErrors().getFirst());
    }

    @Test
    void send_ValidMailWithIncorrectData_FailureResponse() throws IOException, InterruptedException {
        // Set up test data
        CommonMail mail = createTestMail();
        mail.getAttachments()
                .forEach(att -> att.setContent("1"));

        // Set up mocks
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        InputStream inputStream = new ByteArrayInputStream("{\"success\": false, \"errors\": [\"attachment 0: content should be base64 encoded\"]}".getBytes());
        when(mockResponse.statusCode()).thenReturn(400);
        when(mockResponse.body()).thenReturn(inputStream);
        when(mockHttpClient.send(Mockito.any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofInputStream())))
                .thenReturn(mockResponse);

        // Perform call
        CommonResponse response = mailClient.send(mail);

        // Assert
        assertInstanceOf(FailureResponse.class, response);
        assertFalse(response.isSuccess());
        assertFalse(((FailureResponse) response).getErrors().isEmpty());
        assertEquals("attachment 0: content should be base64 encoded", ((FailureResponse) response).getErrors().getFirst());
    }

    @Test
    void send_ValidMailServerError_FailureResponse() throws IOException, InterruptedException {
        // Set up test data
        CommonMail mail = createTestMail();

        // Set up mocks
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        InputStream inputStream = new ByteArrayInputStream("{\"success\": false, \"errors\": [\"Server error\"]}".getBytes());
        when(mockResponse.statusCode()).thenReturn(500);
        when(mockResponse.body()).thenReturn(inputStream);
        when(mockHttpClient.send(Mockito.any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofInputStream())))
                .thenReturn(mockResponse);

        // Perform call
        CommonResponse response = mailClient.send(mail);

        // Assert
        assertInstanceOf(FailureResponse.class, response);
        assertFalse(response.isSuccess());
        assertFalse(((FailureResponse) response).getErrors().isEmpty());
        assertEquals("Server error", ((FailureResponse) response).getErrors().getFirst());
    }

    @Test
    void send_ValidMailNullableResponseBody_ThrowsBaseMailtrapException() throws IOException, InterruptedException {
        // Set up test data
        CommonMail mail = createTestMail();

        // Set up mocks
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        when(mockResponse.statusCode()).thenReturn(200);
        when(mockResponse.body()).thenReturn(null);
        when(mockHttpClient.send(Mockito.any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofInputStream())))
                .thenReturn(mockResponse);

        // Assert
        assertThrows(BaseMailtrapException.class, () -> mailClient.send(mail));
    }

    @Test
    void send_ValidMailInvalidResponseBody_ThrowsJsonException() throws IOException, InterruptedException {
        // Set up test data
        CommonMail mail = createTestMail();

        // Set up mocks
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        InputStream inputStream = new ByteArrayInputStream("{\"success\": false \"errors\": [\"Server error\"]}".getBytes());
        when(mockResponse.statusCode()).thenReturn(500);
        when(mockResponse.body()).thenReturn(inputStream);
        when(mockHttpClient.send(Mockito.any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofInputStream())))
                .thenReturn(mockResponse);

        // Assert
        assertThrows(JsonException.class, () -> mailClient.send(mail));
    }

    @Test
    void send_ValidMailIRequestInterrupted_ThrowsBaseMailtrapException() throws IOException, InterruptedException {
        // Set up test data
        CommonMail mail = createTestMail();

        // Set up mocks
        HttpResponse mockResponse = Mockito.mock(HttpResponse.class);
        InputStream inputStream = new ByteArrayInputStream("{\"success\": false \"errors\": [\"Server error\"]}".getBytes());
        when(mockResponse.statusCode()).thenReturn(500);
        when(mockResponse.body()).thenReturn(inputStream);
        when(mockHttpClient.send(Mockito.any(HttpRequest.class), eq(HttpResponse.BodyHandlers.ofInputStream())))
                .thenThrow(InterruptedException.class);

        // Assert
        assertThrows(BaseMailtrapException.class, () -> mailClient.send(mail));
    }

    // Helper method to create a sample CommonMail object for testing
    private CommonMail createTestMail() {
        var mail = new CommonMail();

        Address from = new Address();
        from.setEmail("sender@example.com");

        Address to = new Address();
        to.setEmail("recipient@example.com");

        Attachment attachment = new Attachment();
        attachment.setFilename("attachment.txt");
        attachment.setType("text/plain");
        attachment.setContent("c2FtcGxlIHRleHQgaW4gdGV4dCBmaWxl");

        mail.setFrom(from);
        mail.setTo(List.of(to));
        mail.setSubject("Sample subject");
        mail.setText("Sample mail text");
        mail.setHtml("<html><body>Test HTML</body></html>");
        mail.setAttachments(List.of(attachment));

        return mail;
    }
}
