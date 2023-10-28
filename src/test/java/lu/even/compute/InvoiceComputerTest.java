package lu.even.compute;

import lu.even.InvoiceExamples;
import lu.even.domain.Invoice;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class InvoiceComputerTest {
    @Test
    public void compute(){
        Invoice invoice = InvoiceExamples.getNotComputed();
        System.out.println(InvoiceComputer.compute(invoice));
        Assertions.assertTrue(invoice.hasSummary());
    }
    @Test
    public void computeNoVat(){
        Invoice invoice = InvoiceExamples.getNotComputedNoVat();
        System.out.println(InvoiceComputer.compute(invoice));
        Assertions.assertFalse(invoice.hasSummary());
    }

}