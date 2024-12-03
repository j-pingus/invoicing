package lu.even.invoicing;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import java.io.File;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class SendEngine implements CommandLineRunner {
    private final JavaMailSender mailSender;

    public static void main(String[] args) {
        SpringApplication.run(SendEngine.class, args);
        logger.info("Finished Sending");
    }

    @Override
    public void run(String... args) throws Exception {
        File destinataires = new File(args[0]);
        var body = FileUtils.readFileToString(new File(args[1]), Charset.defaultCharset());
        String from = "competition@cnw.lu";
        var tos = FileUtils.readLines(destinataires, Charset.defaultCharset());
        List<List<String>> toLists = splitListBySize(tos, 15);
        for (var to : toLists) {
            send(from, to, "reminder: 🎅 Lux Christmas Cup 2024 🎅", body, 15);
        }
    }

    private List<List<String>> splitListBySize(List<String> tos, int expectedSize) {
        int amount = Math.max(tos.size() / expectedSize, 1);
        var grouped = tos.stream().collect(
                Collectors.groupingBy(s -> s.hashCode() % amount)
        );
        return new ArrayList<>(grouped.values());
    }

    public void send(String from, List<String> to, String subject, String body, long waitInMinutes) throws MessagingException, InterruptedException {
        logger.info("Sending to :{}", to);
        MimeMessage message = this.mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setFrom(from);
        helper.setBcc(to.toArray(new String[0]));
        helper.setSubject(subject);
        helper.setText(body, true);
        //helper.addAttachment(pdf.getName(), pdf);
        this.mailSender.send(message);
        logger.info("Mail sent to : '{}'", to);
        try{
        FileUtils.writeStringToFile(
                new File("sent.txt"),
                to.stream().collect(Collectors.joining("\n"))+"\n\n\n",
                Charset.defaultCharset(),
                true
        );
        }catch(Exception e){
            logger.error("Could not log emails sent");
        }

        Thread.sleep(waitInMinutes * 60000L);
    }
}
