package lu.even.invoicing;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import lu.even.invoicing.domain.Invoice;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;
import org.kohsuke.args4j.spi.BooleanOptionHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Profile;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@SpringBootApplication
@Profile("!test")
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
            parser.parseArgument(filterArguments(args,parser));
        } catch (CmdLineException e) {
            logger.error("Error parsing arguments:{}", e.getLocalizedMessage(), e);
            parser.printUsage(System.err);
            System.exit(2);
        }
        if (arguments.prepare) {
            logger.info("Preparing invoices");
            invoiceEngine.init(arguments.templates);

            Collection<Invoice> invoices = invoiceEngine.prepare(arguments.excel, arguments.invoices);
            if (arguments.send) {
                invoiceEngine.send(invoices, arguments.invoices);
            }else{
                logger.info("Not sending invoices (-send)");
            }
        } else if (arguments.send) {
            logger.info("Not preparing invoices (-prepare)");
            invoiceEngine.send(arguments.excel, arguments.invoices);
        } else {
            logger.warn("You asked for nothing (-send) (-prepare)");
        }

    }
    String[] filterArguments(String[] args, CmdLineParser parser) {
        List<String> accepteds = parser.getOptions().stream()
                .filter(opt -> opt.getClass() != BooleanOptionHandler.class)
                .map(opt -> opt.option.toString())
                .toList();
        List<String> acceptedBools = parser.getOptions().stream()
                .filter(opt -> opt.getClass() == BooleanOptionHandler.class)
                .map(opt -> opt.option.toString())
                .toList();
        List<String> stripedArgs = new ArrayList<>();
        for (int i = 0; i < args.length; i++) {
            if (accepteds.contains(args[i])) {
                stripedArgs.add(args[i]);
                stripedArgs.add(args[i + 1]);
            }
            if (acceptedBools.contains(args[i])) {
                stripedArgs.add(args[i]);
            } else {
                logger.warn("Ignoring argument '{}'", args[i]);
            }
        }
        return stripedArgs.toArray(new String[0]);
    }
    @Getter
    @Setter
    static class Arguments {
        @Option(name = "-prepare", usage = "save pdf and mail in a folder without sending")
        private boolean prepare = false;
        @Option(name = "-send", usage = "send prepared invoice")
        private boolean send = false;
        @Option(name = "-invoices", usage = "where to store invoice pdf and mail HTML are prepared")
        private File invoices = new File("./invoices");
        @Option(name = "-excel", usage = "path to the excel file to be processed", required = true)
        private File excel = null;
        @Option(name = "-templates", usage = "path to the folder where to find template files")
        private File templates = new File("./templates");
    }
}
