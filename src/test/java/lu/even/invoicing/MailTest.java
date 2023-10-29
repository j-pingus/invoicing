package lu.even.invoicing;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.apache.commons.io.FileUtils;
import org.apache.poi.util.IOUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import java.io.File;
import java.io.IOException;

@SpringBootTest
@Profile("cnw")
public class MailTest {
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
        helper.setText(FileUtils.readFileToString(htmlFile,"UTF-8"),true);
        helper.addAttachment("file.pdf",pdfFile);
        mailSender.send(message);
    }
}
