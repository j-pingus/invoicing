package lu.even.invoicing.pdf;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.Test;

import java.io.File;
 import java.io.FileOutputStream;
import java.io.IOException;

class PdfProducerTest {

    @Test
    void htmlToPdfTest() throws IOException {
        File html = new File("src/test/resources/html/sample.html");
        String htmlString = FileUtils.readFileToString(html, "UTF-8");
        File pdf = new File("target/test-invoice.pdf");
        try (FileOutputStream fos = new FileOutputStream(pdf)) {
            fos.write(PdfProducer.htmlToPdf(htmlString, html.getParentFile().toURI().toURL()));
        }

    }
}