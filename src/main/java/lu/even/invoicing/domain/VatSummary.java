package lu.even.invoicing.domain;

import com.crabshue.commons.aggregator.Execute;
import lombok.Data;

import java.math.BigDecimal;
@Data
public class VatSummary {
    BigDecimal vat;
    @Execute("sum(this.vat+'.amount')")
    BigDecimal amount;
    @Execute("sum(this.vat+'.vatAmount')")
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
