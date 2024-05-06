package io.mailtrap.testtask.client;

import io.mailtrap.testtask.request.CommonMail;
import io.mailtrap.testtask.response.CommonResponse;

public interface MailtrapClient {
    CommonResponse send(CommonMail mail);
}
