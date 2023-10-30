package lu.even.invoicing;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.test.context.ActiveProfiles;

import java.io.File;
import java.io.IOException;

@SpringBootTest
@ActiveProfiles("cnw")
public class TestMailCnwIT {
    @Autowired
    JavaMailSender mailSender;

    @Test
    public void testMail() throws MessagingException, IOException {
        File htmlFile = new File("target/notVat.html");
        File pdfFile = new File("target/AAAAA-0002.pdf");
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom("bureau@cnw.lu");
        helper.setTo("gerald.even@gmail.com");
        helper.setSubject("Votre facture: dummy");
        helper.setText(FileUtils.readFileToString(htmlFile, "UTF-8"), true);
        helper.addAttachment("file.pdf", pdfFile);
        mailSender.send(message);
    }
}
