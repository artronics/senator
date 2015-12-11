package artronics.senator.core.config;

import artronics.senator.core.db.seeder.DbSeeder;
import artronics.senator.services.ControllerConfigService;
import artronics.senator.services.ControllerSessionService;
import artronics.senator.services.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;

@Configuration
@EnableAutoConfiguration

@ImportResource("classpath:di/sdwn_controller_DI.xml")

@PropertySource({
        "classpath:application.properties",
        "classpath:application-prod.properties"})

@PropertySource("classpath:senator_config.properties")

@ComponentScan({
        "artronics.senator.services.impl",
        "artronics.senator.core"})
public class BeanDefinition
{
    @Autowired
    PacketService packetService;
    @Autowired
    ControllerConfigService configService;
    @Autowired
    ControllerSessionService sessionService;

    @Bean
    DbSeeder dbSeeder()
    {
        return new DbSeeder(configService, sessionService, packetService);
    }
}
