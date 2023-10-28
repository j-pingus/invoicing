package lu.even.domain;

import lombok.Data;

import java.math.BigDecimal;
@Data
public class InvoiceLine {
    String description;
    BigDecimal quantity;
    BigDecimal unitPrice;
    BigDecimal vat;
    BigDecimal vatAmount;
    BigDecimal total;
    BigDecimal totalInclVat;
    public boolean isValid(){
        return unitPrice!=null && quantity!=null;
    }
}
