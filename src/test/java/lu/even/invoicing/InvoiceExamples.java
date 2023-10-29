package lu.even.invoicing;

import lu.even.invoicing.domain.Invoice;
import lu.even.invoicing.domain.InvoiceLine;

import java.math.BigDecimal;
import java.io.File;
public class InvoiceExamples {
    private static final BigDecimal TWO = BigDecimal.valueOf(2);
    private static final BigDecimal BIG = BigDecimal.valueOf(12345);
    public static File TEMPLATE_FOLDER = new File("src/test/resources/templates/");
    public static Invoice getNotComputedNoVat() {
        return new Invoice()
                .addLines(
                        new InvoiceLine().setQuantity(BigDecimal.ONE).setUnitPrice(BigDecimal.TEN),
                        new InvoiceLine().setQuantity(TWO).setUnitPrice(TWO)
                )
                .setNumber("Not computed No VAT")
                .setTotal(BIG);
    }

    public static Invoice getNotComputed() {

        return new Invoice()
                .addLines(
                        new InvoiceLine().setQuantity(BigDecimal.ONE).setUnitPrice(BigDecimal.TEN),
                        new InvoiceLine().setQuantity(TWO).setUnitPrice(TWO).setVat(TWO),
                        new InvoiceLine().setQuantity(TWO).setUnitPrice(TWO).setVat(TWO),
                        new InvoiceLine().setQuantity(BigDecimal.TEN).setUnitPrice(TWO).setVat(BigDecimal.ONE)
                )
                .setNumber("Not computed")
                .setTotal(BIG);
    }
    public static Invoice getNotComputedWithSummary() {

        Invoice dummy= new Invoice()
                .addLines(
                        new InvoiceLine().setQuantity(BigDecimal.ONE).setUnitPrice(BigDecimal.TEN).setDescription("dummy"),
                        new InvoiceLine().setQuantity(TWO).setUnitPrice(TWO).setVat(TWO).setDescription("Following lines left blank intentionally"),
                        new InvoiceLine().setQuantity(TWO).setUnitPrice(TWO).setVat(TWO),
                        new InvoiceLine().setQuantity(BigDecimal.TEN).setUnitPrice(TWO).setVat(BigDecimal.ONE)
                )
                .setNumber("Not computed, VAT Summary")
                .setTotal(BIG);
        dummy.getVatSummary(TWO).addVatAmount(BigDecimal.valueOf(0.4)).addAmount(BigDecimal.valueOf(8));
        return dummy;
    }
}
