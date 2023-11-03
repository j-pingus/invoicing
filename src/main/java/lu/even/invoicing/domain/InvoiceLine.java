package lu.even.invoicing.domain;

import com.crabshue.commons.aggregator.Collect;
import com.crabshue.commons.aggregator.Execute;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class InvoiceLine {
    String description;
    BigDecimal quantity;
    BigDecimal unitPrice;
    @Collect("vat")
    BigDecimal vat;
    @Execute(value = "(this.unitPrice * this.quantity) * this.vat / $BD100", when = "this.vat != null")
    @Collect(value = "eval:this.vat + '.vatAmount'", when = "this.vat != null")
    BigDecimal vatAmount;
    @Execute("this.unitPrice * this.quantity")
    @Collect(value = "eval:this.vat + '.amount'", when = "this.vat != null")
    BigDecimal total;
    @Execute(value = "this.unitPrice * this.quantity * (this.vat + $BD100) / $BD100", when = "this.vat != null")
    @Execute(value = "this.unitPrice * this.quantity", when = "this.vat == null")
    @Collect("invoice.total")
    BigDecimal totalInclVat;

    public boolean isValid() {
        return unitPrice != null && quantity != null;
    }
}
