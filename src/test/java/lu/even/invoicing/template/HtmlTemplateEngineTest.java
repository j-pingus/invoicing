package lu.even.invoicing.template;

import lu.even.invoicing.InvoiceExamples;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

class HtmlTemplateEngineTest {
    InvoiceTemplateEngine invoiceTemplateEngine = new InvoiceTemplateEngine(InvoiceExamples.TEMPLATE_FOLDER);

    @Test
    void renderInvoice() throws IOException {
        String invoiceHtml = invoiceTemplateEngine.renderInvoice(InvoiceExamples.getNotComputedWithSummary());
        FileUtils.writeStringToFile(new File("target/notComputed.html"), invoiceHtml, "UTF-8");
    }

    @Test
    void renderInvoiceNoVat() throws IOException {
        String invoiceHtml = invoiceTemplateEngine.renderInvoice(InvoiceExamples.getNotComputedNoVat());
        FileUtils.writeStringToFile(new File("target/notVat.html"), invoiceHtml, "UTF-8");
    }
}