package artronics.senator.core;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "artronics.senator.core",
        "artronics.senator.core.db.seeder",
        "artronics.senator.mvc.controllers",
        "artronics.senator.services",
        "artronics.senator.repositories",
        "artronics.senator.repositories.jpa",
        "artronics.gsdwn.packet",
        "artronics.gsdwn.model"
})
public class SenatorBootApplication
{
    private static final Logger log = LoggerFactory.getLogger(SenatorBootApplication.class);

    public static void main(String[] args) throws Exception
    {
        log.info("Starting web application.");

        SpringApplication.run(SenatorBootApplication.class,args);
    }
}
