package lu.even.invoicing.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Data
public class Invoice {
    String number;
    Contact recipient;
    List<InvoiceLine> lines;
    BigDecimal total;
    List<VatSummary> summary;

    public Invoice addLines(InvoiceLine... lines) {
        this.lines.addAll(Arrays.asList(lines));
        return this;
    }

    public Invoice() {
        this.lines = new ArrayList<>();
        this.summary = new ArrayList<>();
    }

    public boolean hasSummary() {
        return this.summary != null && !this.summary.isEmpty();
    }
    public VatSummary getVatSummary(BigDecimal vat){
        VatSummary vatSummary = this.summary.stream().filter(s->s.getVat().equals(vat))
                .findFirst()
                .orElse(new VatSummary().setVat(vat));
        if(!this.summary.contains(vatSummary)){
            this.summary.add(vatSummary);
        }
        return vatSummary;
    }

    public void addTotal(BigDecimal value) {
        this.total = this.total.add(value);
    }
}
