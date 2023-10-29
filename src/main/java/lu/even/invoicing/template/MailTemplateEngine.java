package lu.even.invoicing.template;

import lu.even.invoicing.domain.Invoice;

import java.io.File;

public class MailTemplateEngine extends OneTemplateEngine{

    public MailTemplateEngine(File templateFolder) {
        super(templateFolder, "Mail");
    }
    public String renderInvoice(Invoice invoice){
        return super.render("invoice",invoice);
    }
}
