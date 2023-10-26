package lu.even.extractor;

import lu.even.domain.Invoice;

import java.io.InputStream;
import java.util.List;

public interface InvoiceExctractor {
    List<Invoice> extractInvoices(InputStream source) throws ExtractionException;
}
