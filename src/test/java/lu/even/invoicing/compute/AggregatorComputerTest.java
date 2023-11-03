package lu.even.invoicing.compute;

import lu.even.invoicing.InvoiceExamples;
import lu.even.invoicing.domain.Invoice;
import org.junit.jupiter.api.Test;

class AggregatorComputerTest extends InvoiceComputerTestBed {
    @Test
    public void compute() {
        Invoice invoice = InvoiceExamples.getNotComputed();
        System.out.println(AggregatorComputer.compute(invoice));
        assertNotComputed(invoice);
    }

    @Test
    public void computeNoVat() {
        Invoice invoice = InvoiceExamples.getNotComputedNoVat();
        System.out.println(AggregatorComputer.compute(invoice));
        assertNotComputedNoVat(invoice);
    }

}
