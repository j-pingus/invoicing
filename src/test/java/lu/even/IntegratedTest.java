package lu.even;

import lu.even.compute.InvoiceComputer;
import lu.even.domain.Invoice;
import lu.even.extractor.ExcelInvoiceExtractor;
import lu.even.extractor.ExtractionException;
import lu.even.pdf.PdfProducer;
import lu.even.template.HtmlTemplateEngine;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class IntegratedTest {
    @Test
    public void integratedTest() throws IOException, ExtractionException {
        File baseTemplate = new File("src/test/resources/templates/");
        ExcelInvoiceExtractor extractor = new ExcelInvoiceExtractor();
        HtmlTemplateEngine templateEngine = new HtmlTemplateEngine(InvoiceExamples.TEMPLATE_FOLDER, InvoiceExamples.TEMPLATE_TYPE);
        try (FileInputStream excel = new FileInputStream("src/test/resources/excel/Exemple.xlsx")) {
            List<Invoice> extracted = extractor.extractInvoices(excel);
            extracted.forEach(InvoiceComputer::compute);
            for (Invoice invoice : extracted) {
                String html = templateEngine.renderInvoice("Invoice", invoice);
                try (FileOutputStream fos = new FileOutputStream("target/" + invoice.getNumber() + ".pdf")) {
                    fos.write(PdfProducer.htmlToPdf(html, baseTemplate.toURI().toURL()));
                }
            }
        }
    }
}
