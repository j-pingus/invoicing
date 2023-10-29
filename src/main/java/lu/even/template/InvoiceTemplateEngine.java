package lu.even.template;

import lombok.extern.slf4j.Slf4j;
import lu.even.domain.Invoice;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
@Slf4j
public class InvoiceTemplateEngine extends HtmlTemplateEngine{
    Properties invoiceProperties = null;
    SimpleDateFormat dateFormat =null;
    public InvoiceTemplateEngine(File templateFolder, String extension) {
        super(templateFolder, "html");
        File properties = new File(templateFolder,"Invoice.properties");
        if(properties.exists()){
            logger.info("Properties '{}' detected",properties);
            Properties p = new Properties();
            try(FileInputStream fis = new FileInputStream(properties)){
                p.load(fis);
                if(p.containsKey("lang") && p.containsKey("dateFormat")){
                    dateFormat = new SimpleDateFormat(p.getProperty("dateFormat"),
                            Locale.forLanguageTag(p.getProperty("lang")));
                }else{
                    dateFormat = new SimpleDateFormat();
                }
                p.stringPropertyNames().forEach(key->variables.put(key,p.getProperty(key)));
            }catch (IOException e){
                logger.error("Could not load '{}'",properties,e);
            }
        }
    }

    public String renderInvoice(Invoice invoice){
        Map<String,Object> variables = new HashMap<>();
        //FIXME: cache this loading.
        File properties = new File(templateFolder,template+".properties");
         = new SimpleDateFormat("dd MMMMM yyyy");

        variables.put("date",dateFormat.format(new Date()));
        variables.put("invoice",invoice);
        return render(template,variables);
    }

}
