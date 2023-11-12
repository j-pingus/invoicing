package lu.even.invoicing.template;

import lombok.extern.slf4j.Slf4j;

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

    public OneTemplateEngine(File templateFolder, String templateName) {
        this(templateFolder, templateName, "fr", "dd/MM/yyyy");
    }

    public OneTemplateEngine(File templateFolder, String templateName, String lang, String dateFormat) {
        super(templateFolder, "html");
        this.templateName = templateName;
        File properties = new File(templateFolder, templateName + ".properties");
        if (properties.exists()) {
            logger.info("Properties '{}' detected", properties);
            invoiceProperties = new Properties();
            try (FileInputStream fis = new FileInputStream(properties)) {
                invoiceProperties.load(fis);
                this.dateFormat = new SimpleDateFormat(dateFormat,
                        Locale.forLanguageTag(lang));
            } catch (IOException e) {
                logger.error("Could not load '{}'", properties, e);
            }
        }
    }

    public String render(String name, Object object) {
        Map<String, Object> variables = new HashMap<>();
        if (invoiceProperties != null)
            invoiceProperties.stringPropertyNames().forEach(key -> variables.put(key, invoiceProperties.getProperty(key)));
        if (dateFormat != null)
            variables.put("date", dateFormat.format(new Date()));
        variables.put(name, object);
        return render(templateName, variables);
    }

}
