package lu.even.invoicing;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import lu.even.invoicing.compute.InvoiceComputer;
import lu.even.invoicing.configuration.InvoiceConfiguration;
import lu.even.invoicing.domain.Invoice;
import lu.even.invoicing.extractor.ExcelInvoiceExtractor;
import lu.even.invoicing.extractor.ExtractionException;
import lu.even.invoicing.pdf.PdfProducer;
import lu.even.invoicing.template.InvoiceTemplateEngine;
import lu.even.invoicing.template.MailTemplateEngine;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Import;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collection;

@Component
@Import(
        InvoiceConfiguration.class
)
@Slf4j
public class InvoiceEngine {
    public static final String PDF = ".pdf";
    public static final String MAIL = ".mail.html";
    public static final String CHARSET = "UTF-8";
    InvoiceTemplateEngine invoiceTemplateEngine;
    MailTemplateEngine mailTemplateEngine;
    private File templateFolder;
    private final InvoiceConfiguration invoiceConfiguration;
    private final JavaMailSender mailSender;

    public InvoiceEngine(JavaMailSender mailSender, InvoiceConfiguration invoiceConfiguration) {
        this.mailSender = mailSender;
        this.invoiceConfiguration = invoiceConfiguration;
    }

    public void init(File templateFolder) {
        if(!templateFolder.exists()){
            throw new Error("Template folder does not exist:"+templateFolder);
        }
        this.templateFolder = templateFolder;
        this.invoiceTemplateEngine = new InvoiceTemplateEngine(templateFolder);
        this.mailTemplateEngine = new MailTemplateEngine(templateFolder);
    }

    private Collection<Invoice> getInvoices(File excelFile) throws IOException, ExtractionException {
        ExcelInvoiceExtractor extractor = new ExcelInvoiceExtractor();
        try (FileInputStream excel = new FileInputStream(excelFile)) {
            return extractor.extractInvoices(excel);
        }
    }

    public Collection<Invoice> prepare(File excelFile, File destination) throws IOException, ExtractionException {
        if(!destination.exists()){
            throw new Error("destination must exist:"+destination);
        }
        Collection<Invoice> extracted = getInvoices(excelFile);
        extracted.forEach(InvoiceComputer::compute);
        for (Invoice invoice : extracted) {
            String invoiceFileName = computeInvoiceFileName(invoice);
            File pdf = new File(destination, invoiceFileName + PDF);
            File mailFile = new File(destination, invoiceFileName + MAIL);
            String html = invoiceTemplateEngine.renderInvoice(invoice);
            String mail = mailTemplateEngine.renderInvoice(invoice);
            FileUtils.writeStringToFile(mailFile, mail, CHARSET);
            FileUtils.writeByteArrayToFile(pdf, PdfProducer.htmlToPdf(html, templateFolder.toURI().toURL()));
            logger.info("invoice prepared : '{}'", pdf);
        }
        return extracted;
    }

    private String computeInvoiceFileName(Invoice invoice) {
        return invoice.getNumber();
    }

    public void send(File excelFile, File destination) throws ExtractionException, IOException {
        send(getInvoices(excelFile), destination);
    }

    public void send(Collection<Invoice> invoices, File destination) {
        for (Invoice invoice : invoices) {
            String invoiceFileName = computeInvoiceFileName(invoice);
            File pdf = new File(destination, invoiceFileName + PDF);
            File mail = new File(destination, invoiceFileName + MAIL);
            if (!pdf.exists() || !mail.exists()) {
                logger.error("Pdf('{}') or mail('{}') not found, no mail sent:", pdf, mail);
            }
            try {
                String html = FileUtils.readFileToString(mail, CHARSET);
                MimeMessage message = this.mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message, true);
                helper.setFrom("<" + invoiceConfiguration.getSender().getName() + ">" + invoiceConfiguration.getSender().getEmail());
                helper.setTo(invoice.getRecipient().getEmail());
                helper.setSubject(invoiceConfiguration.getSender().getName() + ":" + invoiceConfiguration.getMailSubject() + invoice.getNumber());
                helper.setText(html, true);
                helper.addAttachment(pdf.getName(), pdf);
                this.mailSender.send(message);
                logger.info("Mail sent to : '{}'", invoice.getRecipient().getEmail());
                pdf.delete();
                mail.delete();
            } catch (IOException e) {
                logger.info("Could not read html file for mail:{}", mail, e);
            } catch (MessagingException e) {
                logger.info("Could not send mail:{}", mail, e);
            }
        }
    }
}
