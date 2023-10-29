package lu.even.invoicing.extractor;

import lu.even.invoicing.domain.Invoice;
import org.junit.jupiter.api.Test;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.List;

class ExcelInvoiceExtractorTest {
    @Test
    public void extractInvoice() throws ExtractionException, IOException {
        ExcelInvoiceExtractor extractor = new ExcelInvoiceExtractor();
        try (FileInputStream excel = new FileInputStream("src/test/resources/excel/Exemple.xlsx")) {
            List<Invoice> extracted = extractor.extractInvoices(excel);
            System.out.println(extracted);
        }
    }
}