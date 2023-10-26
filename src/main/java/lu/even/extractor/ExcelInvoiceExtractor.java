package lu.even.extractor;

import lombok.extern.slf4j.Slf4j;
import lu.even.domain.Contact;
import lu.even.domain.Invoice;
import lu.even.domain.InvoiceLine;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class ExcelInvoiceExtractor implements InvoiceExctractor {
    private static String SHEET_INVOICE = "Invoices";
    private static int COL_NUMBER = 0;
    private static int COL_RECIPIENT = 1;
    private static int COL_EMAIL = 2;
    private static int COL_DESCRIPTION = 3;
    private static int COL_QUANTITY = 4;
    private static int COL_UNIT_PRICE = 5;
    private static int COL_VAT = 6;
    private static int COL_SENT = 7;

    @Override
    public List<Invoice> extractInvoices(InputStream source) throws ExtractionException {
        List<Invoice> extracted = new ArrayList<>();
        try {
            Workbook workbook = new XSSFWorkbook(source);
            Sheet sheet1 = workbook.getSheet(SHEET_EMITER);
            Contact emitter = new Contact()
                    .setName(
                            sheet1.getRow(ROW_NAME).getCell(1).getStringCellValue())
                    .setAdress(sheet1.getRow(ROW_ADDRESS).getCell(1).getStringCellValue())
                    .setEmail(sheet1.getRow(ROW_EMAIL).getCell(1).getStringCellValue());
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
                                        .setAdress(getString(row, COL_RECIPIENT))
                                        .setEmail(getString(row, COL_EMAIL))
                                )
                                .setEmiter(emitter);
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

    String getString(Row row, int cellId) {
        return row.getCell(cellId).getStringCellValue();
    }

    BigDecimal getNumber(Row row, int cellId) {
        double value = row.getCell(cellId).getNumericCellValue();
        return BigDecimal.valueOf(value);
    }
}
