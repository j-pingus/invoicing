package lu.even.invoicing.extractor;

import lombok.extern.slf4j.Slf4j;
import lu.even.invoicing.domain.Contact;
import lu.even.invoicing.domain.Invoice;
import lu.even.invoicing.domain.InvoiceLine;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.poi.ss.usermodel.CellType.NUMERIC;
import static org.apache.poi.ss.usermodel.CellType.STRING;

@Slf4j
public class ExcelInvoiceExtractor implements InvoiceExctractor {
    private static int COL = 0;
    private static final String SHEET_INVOICE = "Invoices";
    private static final int COL_NUMBER = COL++;
    private static final int COL_NAME = COL++;
    private static final int COL_RECIPIENT = COL++;
    private static final int COL_EMAIL = COL++;
    private static final int COL_DESCRIPTION = COL++;
    private static final int COL_QUANTITY = COL++;
    private static final int COL_UNIT_PRICE = COL++;
    private static final int COL_VAT = COL++;
    private static final int COL_SENT = COL++;

    @Override
    public List<Invoice> extractInvoices(InputStream source) throws ExtractionException {
        List<Invoice> extracted = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(source);
            Sheet sheet = workbook.getSheet(SHEET_INVOICE);
            Invoice invoice = new Invoice();
            invoice.setNumber(null);
            for (Row row : sheet) {
                if (row.getRowNum() > 0) {
                    String invoiceId = getString(row, COL_NUMBER);
                    boolean sent = "Y".equals(getString(row, COL_SENT));
                    if (invoiceId == null) continue;
                    if (!invoiceId.equals(invoice.getNumber())) {
                        invoice = new Invoice().setNumber(invoiceId);
                        if (!sent) {
                            extracted.add(invoice);
                        }
                        invoice
                                .setRecipient(new Contact()
                                        .setName(getString(row, COL_NAME))
                                        .setAdress(getString(row, COL_RECIPIENT))
                                        .setEmail(getString(row, COL_EMAIL))
                                );
                    }

                    invoice.getLines().add(
                            new InvoiceLine()
                                    .setDescription(getString(row, COL_DESCRIPTION))
                                    .setUnitPrice(getNumber(row, COL_UNIT_PRICE))
                                    .setVat(getNumber(row, COL_VAT))
                                    .setQuantity(getNumber(row, COL_QUANTITY))
                    );
                }
            }
        } catch (IOException e) {
            logger.error("Could not read excel", e);
            throw new ExtractionException("Could not read Excel");
        }
        return extracted;
    }

    Optional<Cell> getCell(Row row, int cellId, CellType type) {
        Cell cell = row.getCell(cellId);
        if (cell == null) return Optional.empty();
        if (cell.getCellType() != type) return Optional.empty();
        return Optional.of(cell);
    }

    String getString(Row row, int cellId) {
        return getCell(row, cellId, STRING).map(Cell::getStringCellValue).orElse(null);
    }

    BigDecimal getNumber(Row row, int cellId) {
        return getCell(row, cellId, NUMERIC)
                .map(Cell::getNumericCellValue)
                .map(BigDecimal::valueOf)
                .orElse(null);
    }
}
