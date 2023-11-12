package lu.even.invoicing.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "invoicing")
@Data
public class InvoiceConfiguration {
    String mailSubject;
    @NestedConfigurationProperty
    Sender sender;
    String lang;
    String dateFormat;
}
