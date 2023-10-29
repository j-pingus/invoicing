package lu.even.invoicing.domain;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class VatSummary {
    BigDecimal vat;
    BigDecimal amount;
    BigDecimal vatAmount;
    public VatSummary addAmount(BigDecimal amount){
        this.amount = this.amount.add(amount);
        return this;
    }
    public VatSummary addVatAmount(BigDecimal vatAmount){
        this.vatAmount = this.vatAmount.add(vatAmount);
        return this;
    }
    public VatSummary() {
        this.amount=BigDecimal.ZERO;
        this.vatAmount=BigDecimal.ZERO;
    }
}
