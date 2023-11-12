package lu.even.invoicing;

import lombok.extern.slf4j.Slf4j;
import lu.even.invoicing.compute.InvoiceComputer;
import lu.even.invoicing.domain.Invoice;
import lu.even.invoicing.extractor.ExcelInvoiceExtractor;
import lu.even.invoicing.extractor.ExtractionException;
import lu.even.invoicing.pdf.PdfProducer;
import lu.even.invoicing.template.InvoiceTemplateEngine;
import lu.even.invoicing.template.MailTemplateEngine;
import lu.even.invoicing.template.OneTemplateEngine;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
@Slf4j
public class IntegratedTest {
    @Test
    public void integratedTest() throws IOException, ExtractionException {
        File baseTemplate = new File("src/test/resources/templates/");
        ExcelInvoiceExtractor extractor = new ExcelInvoiceExtractor();
        InvoiceTemplateEngine templateEngine = new InvoiceTemplateEngine(InvoiceExamples.TEMPLATE_FOLDER,"fr","dd/MM/yyyy");
        MailTemplateEngine templateEngine2 = new MailTemplateEngine(InvoiceExamples.TEMPLATE_FOLDER,"fr","dd/MM/yyyy");
        try (FileInputStream excel = new FileInputStream("src/test/resources/excel/Exemple.xlsx")) {
            List<Invoice> extracted = extractor.extractInvoices(excel);
            extracted.forEach(InvoiceComputer::compute);
            for (Invoice invoice : extracted) {
                String html = templateEngine.renderInvoice(invoice);
                String mail = templateEngine2.renderInvoice(invoice);
                FileUtils.writeStringToFile(new File("target/"+ invoice.getNumber() + ".html"),html);
                FileUtils.writeStringToFile(new File("target/"+ invoice.getNumber() + ".mail.html"),mail);
                try (FileOutputStream fos = new FileOutputStream("target/" + invoice.getNumber() + ".pdf")) {
                    fos.write(PdfProducer.htmlToPdf(html, baseTemplate.toURI().toURL()));
                    logger.info("Pdf produced:{}","target/" + invoice.getNumber() + ".pdf");
                }
            }
        }
    }
}
