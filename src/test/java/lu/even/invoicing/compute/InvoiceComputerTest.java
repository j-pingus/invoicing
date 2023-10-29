package lu.even.invoicing.compute;

import lu.even.invoicing.InvoiceExamples;
import lu.even.invoicing.domain.Invoice;
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