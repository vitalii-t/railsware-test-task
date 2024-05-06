# Simple Mailtrap Java Client

This Library offers integration with the [official SEND API](https://api-docs.mailtrap.io/) for [Mailtrap](https://mailtrap.io).

Quickly add email sending functionality to your Java application with Mailtrap.

## Java Version

Requires JDK 11 or higher

## Usage

### Dependency
Maven dependency
```xml

<dependency>
    <groupId>io.mailtrap.testtask</groupId>
    <artifactId>java-sdk-test-task</artifactId>
    <version>1.0</version>
</dependency>
```
Gradle Groovy dependency
```groovy
implementation 'io.mailtrap.testtask:java-sdk-test-task:1.0'
```
Gradle Kotlin dependency
```groovy
implementation("io.mailtrap.testtask:java-sdk-test-task:1.0")
```

### Minimal
```java
import io.mailtrap.testtask.client.MailtrapClient;
import io.mailtrap.testtask.client.SendMailMailtrapClient;
import io.mailtrap.testtask.request.Address;
import io.mailtrap.testtask.request.Attachment;
import io.mailtrap.testtask.request.CommonMail;

import java.util.List;

public class TestMailtrapJavaClient {
    public static void main(String[] args) {
        // By default, it would call sandbox API
        MailtrapClient client = new SendMailMailtrapClient("<your token>", 0);

        var mail = new CommonMail();

        var fromAddress = new Address();
        from.setEmail("sender@example.com");

        var toAddress = new Address();
        to.setEmail("recipient@example.com");

        var attachment = new Attachment();
        attachment.setFilename("attachment.txt");
        attachment.setType("text/plain");
        attachment.setContent("c2FtcGxlIHRleHQgaW4gdGV4dCBmaWxl");

        mail.setFrom(fromAddress);
        mail.setTo(Collections.singletonList(toAddress));
        mail.setSubject("Sample subject");
        mail.setText("Sample mail text");
        mail.setHtml("<html><body>Test HTML</body></html>");
        mail.setAttachments(Collections.singletonList(attachment));

        System.out.println(client.send(mail));
    }
}
```
## Contributing

Bug reports and pull requests are welcome on [GitHub](https://github.com/vitalii-t/railsware-test-task).