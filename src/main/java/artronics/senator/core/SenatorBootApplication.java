package artronics.senator.core;

import artronics.senator.repositories.RepositoryConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.ImportResource;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@EnableWebMvc
@Configuration
@EnableAutoConfiguration
@ImportResource("classpath:senator-beans.xml")
@Import(RepositoryConfig.class)
public class SenatorBootApplication
{
    private static final Logger log = LoggerFactory.getLogger(SenatorBootApplication.class);

    public static void main(String[] args) throws Exception
    {
        log.info("Starting web application.");
        SpringApplication.run(SenatorBootApplication.class,args);
    }
}
