package lu.even.invoicing.template;

import lu.even.invoicing.domain.Invoice;

import java.io.File;

public class InvoiceTemplateEngine extends OneTemplateEngine {

    public InvoiceTemplateEngine(File templateFolder, String lang, String dateFormat) {
        super(templateFolder, "Invoice", lang, dateFormat);
    }

    public String renderInvoice(Invoice invoice) {
        return super.render("invoice", invoice);
    }
}
