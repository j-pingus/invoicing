package lu.even.domain;

import lombok.Data;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Data
public class Invoice {
    String number;
    Contact emiter;
    Contact recipient;
    List<InvoiceLine> lines;
    BigDecimal total;

    public Invoice() {
        this.lines = new ArrayList<>();
    }
}
