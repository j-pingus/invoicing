package lu.even.template;

import lombok.extern.slf4j.Slf4j;
import lu.even.domain.Invoice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

@Slf4j
public class OneTemplateEngine extends HtmlTemplateEngine {
    Properties invoiceProperties = null;
    SimpleDateFormat dateFormat = null;
    String templateName;
    public OneTemplateEngine(File templateFolder,String templateName) {
        super(templateFolder, "html");
        this.templateName = templateName;
        File properties = new File(templateFolder, templateName+ ".properties");
        if (properties.exists()) {
            logger.info("Properties '{}' detected", properties);
            invoiceProperties = new Properties();
            try (FileInputStream fis = new FileInputStream(properties)) {
                invoiceProperties.load(fis);
                if (invoiceProperties.containsKey("lang") && invoiceProperties.containsKey("dateFormat")) {
                    dateFormat = new SimpleDateFormat(invoiceProperties.getProperty("dateFormat"),
                            Locale.forLanguageTag(invoiceProperties.getProperty("lang")));
                } else {
                    dateFormat = new SimpleDateFormat();
                }
            } catch (IOException e) {
                logger.error("Could not load '{}'", properties, e);
            }
        }
    }

    public String renderInvoice(Invoice invoice) {
        Map<String, Object> variables = new HashMap<>();
        if (invoiceProperties != null)
            invoiceProperties.stringPropertyNames().forEach(key -> variables.put(key, invoiceProperties.getProperty(key)));
        if (dateFormat != null)
            variables.put("date", dateFormat.format(new Date()));
        variables.put("invoice", invoice);
        return render(templateName, variables);
    }

}
