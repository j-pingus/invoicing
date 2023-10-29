package lu.even.invoicing.template;

import lu.even.invoicing.domain.Invoice;

import java.io.File;

public class InvoiceTemplateEngine extends OneTemplateEngine{

    public InvoiceTemplateEngine(File templateFolder) {
        super(templateFolder, "Invoice");
    }
    public String renderInvoice(Invoice invoice){
        return super.render("invoice",invoice);
    }
}
