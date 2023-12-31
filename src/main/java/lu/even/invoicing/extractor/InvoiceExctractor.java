package lu.even.invoicing.extractor;

import lu.even.invoicing.domain.Invoice;

import java.io.InputStream;
import java.util.List;

public interface InvoiceExctractor {
    List<Invoice> extractInvoices(InputStream source) throws ExtractionException;
}
