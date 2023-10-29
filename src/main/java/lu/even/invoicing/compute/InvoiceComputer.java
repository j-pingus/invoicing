package lu.even.invoicing.compute;

import lu.even.invoicing.domain.Invoice;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class InvoiceComputer {
    public static Invoice compute(Invoice invoice) {
        invoice.setTotal(BigDecimal.ZERO);
        invoice.getLines().forEach(invoiceLine -> {
            if (invoiceLine.isValid()) {
                BigDecimal quantity = invoiceLine.getQuantity();
                if (isIntegerValue(quantity)) {
                    invoiceLine.setQuantity(quantity.setScale(0, RoundingMode.HALF_UP));
                }
                invoiceLine.setUnitPrice(
                        invoiceLine.getUnitPrice().setScale(2, RoundingMode.HALF_UP)
                );
                BigDecimal total = invoiceLine
                        .getQuantity()
                        .multiply(invoiceLine.getUnitPrice())
                        .setScale(2, RoundingMode.HALF_UP);
                invoiceLine.setTotal(total).setTotalInclVat(total);
                if (invoiceLine.getVat() != null) {
                    BigDecimal vat = invoiceLine.getVat();
                    if (isIntegerValue(vat)) {
                        invoiceLine.setVat(vat.setScale(0, RoundingMode.HALF_UP));
                    }
                    BigDecimal vatAmount = total
                            .multiply(invoiceLine.getVat())
                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
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

    private static boolean isIntegerValue(BigDecimal bd) {
        return bd.signum() == 0 || bd.scale() <= 0 || bd.stripTrailingZeros().scale() <= 0;
    }
}
