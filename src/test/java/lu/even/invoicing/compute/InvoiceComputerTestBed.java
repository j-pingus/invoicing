package lu.even.invoicing.compute;

import lu.even.invoicing.domain.Invoice;
import lu.even.invoicing.domain.VatSummary;
import org.hamcrest.Matchers;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class InvoiceComputerTestBed {
    public void assertNotComputed(Invoice invoice) {
        assertTrue(invoice.hasSummary());
        assertThat(BigDecimal.valueOf(4), Matchers.comparesEqualTo(invoice.getLines().get(1).getTotal()));
        assertThat(BigDecimal.valueOf(0.08), Matchers.comparesEqualTo(invoice.getLines().get(1).getVatAmount()));
        assertThat(BigDecimal.valueOf(4.08), Matchers.comparesEqualTo(invoice.getLines().get(1).getTotalInclVat()));
        VatSummary summary = invoice.getVatSummary(BigDecimal.ONE);
        assertNotNull(summary);
        assertThat(BigDecimal.valueOf(0.20), Matchers.comparesEqualTo(summary.getVatAmount()));
        assertThat(BigDecimal.valueOf(20), Matchers.comparesEqualTo(summary.getAmount()));
        assertThat(BigDecimal.valueOf(38.36), Matchers.comparesEqualTo(invoice.getTotal()));
    }

    public void assertNotComputedNoVat(Invoice invoice) {
        assertFalse(invoice.hasSummary());
        assertThat(BigDecimal.valueOf(4), Matchers.comparesEqualTo(invoice.getLines().get(1).getTotal()));
        assertNull(invoice.getLines().get(1).getVatAmount());
        assertThat(BigDecimal.valueOf(4), Matchers.comparesEqualTo(invoice.getLines().get(1).getTotalInclVat()));
        assertThat(BigDecimal.valueOf(14), Matchers.comparesEqualTo(invoice.getTotal()));
    }


}
