package lu.even.invoicing.template;

import lu.even.invoicing.domain.Invoice;

import java.io.File;

public class MailTemplateEngine extends OneTemplateEngine {

    public MailTemplateEngine(File templateFolder, String lang, String dateFormat) {
        super(templateFolder, "Mail", lang, dateFormat);
    }

    public String renderInvoice(Invoice invoice) {
        return super.render("invoice", invoice);
    }
}
