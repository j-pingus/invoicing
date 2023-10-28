package lu.even.template;

import lu.even.InvoiceExamples;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;

class HtmlTemplateEngineTest {

    @Test
    void renderInvoice() throws IOException {
        HtmlTemplateEngine engine = new HtmlTemplateEngine();
        String invoiceHtml = engine.renderInvoice("Invoice",
                InvoiceExamples.getNotComputedWithSummary());
        FileUtils.writeStringToFile(new File("target/notComputed.html"), invoiceHtml, "UTF-8");
    }
    @Test
    void renderInvoiceNoVat() throws IOException {
        HtmlTemplateEngine engine = new HtmlTemplateEngine();
        String invoiceHtml = engine.renderInvoice("Invoice",
                InvoiceExamples.getNotComputedNoVat());
        FileUtils.writeStringToFile(new File("target/notVat.html"), invoiceHtml, "UTF-8");
    }
}