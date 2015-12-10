package artronics.senator.core.config;

import artronics.senator.core.SenatorConfig;
import artronics.senator.core.db.seeder.DbSeeder;
import artronics.senator.services.ControllerConfigService;
import artronics.senator.services.ControllerSessionService;
import artronics.senator.services.PacketService;
import artronics.senator.services.impl.ControllerConfigServiceImpl;
import artronics.senator.services.impl.ControllerSessionServiceImpl;
import artronics.senator.services.impl.PacketServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.*;

@Configuration
@ImportResource("classpath:senator-beans.xml")
@ComponentScan({"artronics.senator.core"})
public class BeanDefinition
{
    @Autowired
    PacketService packetService;
    @Autowired
    ControllerConfigService configService;
    @Autowired
    ControllerSessionService sessionService;
    @Bean
    DbSeeder dbSeeder(){
        return new DbSeeder(configService,sessionService,packetService);
    }
}
