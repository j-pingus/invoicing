package lu.even.invoicing;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class Main implements CommandLineRunner {
    public static void main(String[] args) {
        SpringApplication.run(Main.class,args);
        logger.info("Finished Invoicing");
    }

    @Override
    public void run(String... args) throws Exception {
        logger.info("Blah!");
    }
}