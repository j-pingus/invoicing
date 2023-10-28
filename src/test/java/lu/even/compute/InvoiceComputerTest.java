package lu.even.compute;

import lu.even.domain.Invoice;
import lu.even.domain.InvoiceLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class InvoiceComputerTest {
    private static BigDecimal TWO = BigDecimal.valueOf(2);
    @Test
    public void compute(){
        Invoice invoice = new Invoice()
                .addLines(
                        new InvoiceLine().setQuantity(BigDecimal.ONE).setUnitPrice(BigDecimal.TEN),
                        new InvoiceLine().setQuantity(TWO).setUnitPrice(TWO).setVat(TWO),
                        new InvoiceLine().setQuantity(TWO).setUnitPrice(TWO).setVat(TWO),
                        new InvoiceLine().setQuantity(BigDecimal.TEN).setUnitPrice(TWO).setVat(BigDecimal.ONE)
                );
        System.out.println(InvoiceComputer.compute(invoice));
        Assertions.assertEquals(true,invoice.hasSummary());
    }
    @Test
    public void computeNoVat(){
        Invoice invoice = new Invoice()
                .addLines(
                        new InvoiceLine().setQuantity(BigDecimal.ONE).setUnitPrice(BigDecimal.TEN),
                        new InvoiceLine().setQuantity(TWO).setUnitPrice(TWO)
                );
        System.out.println(InvoiceComputer.compute(invoice));
        Assertions.assertEquals(false,invoice.hasSummary());
    }

}