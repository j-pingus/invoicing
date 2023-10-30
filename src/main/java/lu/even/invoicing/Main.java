package lu.even.invoicing;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lu.even.invoicing.domain.Invoice;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Collection;

@SpringBootApplication
@Slf4j
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
        logger.info("Finished Invoicing");
    }

    @Autowired
    InvoiceEngine invoiceEngine;

    @Override
    public void run(String... args) throws Exception {
        Arguments arguments = new Arguments();
        CmdLineParser parser = new CmdLineParser(arguments);
        try {
            parser.parseArgument(args);
        } catch (CmdLineException e) {
            logger.error("Error parsing arguments:{}", e.getLocalizedMessage(), e);
            parser.printUsage(System.err);
        }
        if (arguments.prepare) {
            invoiceEngine.init(arguments.templates);

            Collection<Invoice> invoices = invoiceEngine.prepare(arguments.excel, arguments.invoices);
            if (arguments.send) {
                invoiceEngine.send(invoices, arguments.invoices);
            }
        } else if (arguments.send) {
            invoiceEngine.send(arguments.excel, arguments.invoices);
        }

    }

    @Getter
    @Setter
    static class Arguments {
        @Option(name = "-prepare", usage = "save pdf and mail in a folder without sending", aliases = "-p")
        private boolean prepare = true;
        @Option(name = "-send", usage = "send prepared invoice", aliases = "-s")
        private boolean send = false;
        @Option(name = "-invoices", usage = "where to store invoice pdf and mail HTML are prepared", aliases = "-i")
        private File invoices = new File("./invoices");
        @Option(name = "-excel", usage = "path to the excel file to be processed", aliases = "-e", required = true)
        private File excel = null;
        @Option(name = "-templates", usage = "path to the folder where to find template files", aliases = "-t")
        private File templates = new File("./templates");
    }
}
