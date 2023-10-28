package lu.even;

import lu.even.domain.Invoice;
import lu.even.domain.InvoiceLine;

import java.math.BigDecimal;

public class InvoiceExamples {
    private static BigDecimal TWO = BigDecimal.valueOf(2);
    private static BigDecimal BIG = BigDecimal.valueOf(12345);

    public static Invoice getNotComputedNoVat() {
        return new Invoice()
                .addLines(
                        new InvoiceLine().setQuantity(BigDecimal.ONE).setUnitPrice(BigDecimal.TEN),
                        new InvoiceLine().setQuantity(TWO).setUnitPrice(TWO)
                )
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
                .setTotal(BIG);
    }
    public static Invoice getNotComputedWithSummary() {

        Invoice dummy= new Invoice()
                .addLines(
                        new InvoiceLine().setQuantity(BigDecimal.ONE).setUnitPrice(BigDecimal.TEN),
                        new InvoiceLine().setQuantity(TWO).setUnitPrice(TWO).setVat(TWO),
                        new InvoiceLine().setQuantity(TWO).setUnitPrice(TWO).setVat(TWO),
                        new InvoiceLine().setQuantity(BigDecimal.TEN).setUnitPrice(TWO).setVat(BigDecimal.ONE)
                )
                .setTotal(BIG);
        dummy.getVatSummary(TWO).addVatAmount(BigDecimal.valueOf(0.4)).addAmount(BigDecimal.valueOf(8));
        return dummy;
    }
}
