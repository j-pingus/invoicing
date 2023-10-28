package lu.even.compute;

import lu.even.domain.Invoice;
import lu.even.domain.InvoiceLine;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InvoiceComputer {
    public static Invoice compute(Invoice invoice) {
        invoice.setTotal(BigDecimal.ZERO);
        invoice.getLines().forEach(invoiceLine -> {
            if (invoiceLine.isValid()) {

                BigDecimal total = invoiceLine.getQuantity().multiply(invoiceLine.getUnitPrice());
                invoiceLine.setTotal(total).setTotalInclVat(total);
                if (invoiceLine.getVat() != null) {
                    BigDecimal vatAmount = total.multiply(invoiceLine.getVat()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
                    invoiceLine.setVatAmount(vatAmount).setTotalInclVat(total.add(vatAmount));
                    invoice.getVatSummary(invoiceLine.getVat())
                            .addAmount(total)
                            .addVatAmount(vatAmount);
                }
                invoice.addTotal(invoiceLine.getTotalInclVat());
            }
        });
        return invoice;
    }
}
