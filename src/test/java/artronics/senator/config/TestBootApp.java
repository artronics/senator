package artronics.senator.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@SpringBootApplication
@Configuration
//@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "artronics.senator.core",
        "artronics.senator.core.db.seeder",
        "artronics.senator.mvc.controllers",
})
public class TestBootApp
{
}
