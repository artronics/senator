package artronics.senator.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
//@EnableAutoConfiguration
@ComponentScan(basePackages = {
        "artronics.senator.core",
        "artronics.senator.core.db.seeder",
        "artronics.senator.mvc.controllers",
})
@ImportResource("classpath:senator-beans-test.xml")
public class TestBootApp
{
}
