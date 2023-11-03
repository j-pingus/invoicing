package lu.even.invoicing.compute;

import com.crabshue.commons.aggregator.AggregatorContext;
import com.crabshue.commons.aggregator.AggregatorProcessing;
import com.crabshue.commons.aggregator.model.AggregatorConfiguration;
import lu.even.invoicing.domain.Invoice;
import lu.even.invoicing.domain.VatSummary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class AggregatorComputer implements AggregatorProcessing {
    public static Invoice compute(Invoice invoice) {
        AggregatorConfiguration configuration = new AggregatorConfiguration()
                .setAnalysedPackages(List.of("lu.even.invoicing.domain"))
                .setProcessings(List.of(AggregatorComputer.class.getName()));
        AggregatorContext context = AggregatorContext.builder()
                .config(configuration)
                .build();
        context.addVariable("BD100", BigDecimal.valueOf(100));
        return context.process(invoice);

    }

    @Override
    public void preProcess(Object o, AggregatorContext aggregatorContext) {

    }

    @Override
    public void postProcess(Object o, AggregatorContext context) {
        if (o instanceof Invoice) {
            Set<BigDecimal> vats = context.asSet("vat");
            ((Invoice) o).setSummary(
                    vats.stream()
                            .map(vat -> new VatSummary().setVat(vat))
                            .map(context::process)
                            .collect(Collectors.toList()));
        }
    }
}
