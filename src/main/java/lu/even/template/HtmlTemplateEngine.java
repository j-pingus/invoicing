package lu.even.template;

import lombok.extern.slf4j.Slf4j;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templatemode.TemplateMode;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;

import java.io.IOException;
import java.io.StringWriter;
import java.util.Locale;
import java.util.Map;

@Slf4j
public class HtmlTemplateEngine {
    private TemplateEngine templateEngine;

    public HtmlTemplateEngine() {
        ClassLoaderTemplateResolver resolver = new ClassLoaderTemplateResolver();
        resolver.setTemplateMode(TemplateMode.HTML);
        resolver.setPrefix("/templates/");
        resolver.setSuffix(".html");
        this.templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(resolver);

    }

    public String render(String template, Map<String, Object> variables) {
        try (StringWriter writer = new StringWriter()) {
            templateEngine.process(template, new Context(Locale.getDefault(), variables), writer);
            return writer.toString();
        } catch (IOException e) {
            logger.error("error occured processing template '{}' with context '{}'", template, variables, e);
            return "error occured processing template, see logs";
        }
    }
}
