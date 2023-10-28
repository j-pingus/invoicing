package lu.even.compute;

import lu.even.InvoiceExamples;
import lu.even.domain.Invoice;
import lu.even.domain.InvoiceLine;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class InvoiceComputerTest {
    @Test
    public void compute(){
        Invoice invoice = InvoiceExamples.getNotComputed();
        System.out.println(InvoiceComputer.compute(invoice));
        Assertions.assertEquals(true,invoice.hasSummary());
    }
    @Test
    public void computeNoVat(){
        Invoice invoice = InvoiceExamples.getNotComputedNoVat();
        System.out.println(InvoiceComputer.compute(invoice));
        Assertions.assertEquals(false,invoice.hasSummary());
    }

}