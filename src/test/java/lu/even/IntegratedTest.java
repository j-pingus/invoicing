package lu.even;

import lu.even.compute.InvoiceComputer;
import lu.even.domain.Invoice;
import lu.even.extractor.ExcelInvoiceExtractor;
import lu.even.extractor.ExtractionException;
import lu.even.pdf.PdfProducer;
import lu.even.template.HtmlTemplateEngine;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;

public class IntegratedTest {
    @Test
    public void integratedTest() throws IOException, ExtractionException {
        ExcelInvoiceExtractor extractor = new ExcelInvoiceExtractor();
        HtmlTemplateEngine templateEngine = new HtmlTemplateEngine();
        try (FileInputStream excel = new FileInputStream("src/test/resources/excel/Exemple.xlsx")) {
            List<Invoice> extracted = extractor.extractInvoices(excel);
            extracted.forEach(InvoiceComputer::compute);
            for (Invoice invoice : extracted) {
                String html = templateEngine.renderInvoice("Invoice", invoice);
                try (FileOutputStream fos = new FileOutputStream("target/" + invoice.getNumber() + ".pdf")) {
                    fos.write(PdfProducer.htmlToPdf(html, new File("src/test/resources/templates").toURI().toURL()));
                }
            }
        }
    }
}
