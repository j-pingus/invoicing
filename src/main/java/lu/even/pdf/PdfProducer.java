package lu.even.pdf;

import org.xhtmlrenderer.layout.SharedContext;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;

public class PdfProducer {
    public static byte[] htmlToPdf(String html, URL baseUrl) throws IOException {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            ITextRenderer renderer = new ITextRenderer();
            SharedContext sharedContext = renderer.getSharedContext();
            sharedContext.setPrint(true);
            sharedContext.setInteractive(false);
            renderer.setDocumentFromString(html, baseUrl.toString());
            renderer.layout();
            renderer.createPDF(outputStream);
            outputStream.flush();
            return outputStream.toByteArray();
        }
    }
}
