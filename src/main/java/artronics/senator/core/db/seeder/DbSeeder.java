package artronics.senator.core.db.seeder;

import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerSession;
import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.core.db.FakePacketFactory;
import artronics.senator.services.ControllerConfigService;
import artronics.senator.services.ControllerSessionService;
import artronics.senator.services.PacketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DbSeeder
{
    PacketService packetService;

    ControllerConfigService configService;

    ControllerSessionService sessionService;
    FakePacketFactory packetFactory = new FakePacketFactory();
    private String controllerIp = "192.168.10.10";
    private long sessionId;

    @Autowired
    public DbSeeder(ControllerConfigService configService,
                    ControllerSessionService sessionService,
                    PacketService packetService)
    {
        this.configService = configService;
        this.sessionService = sessionService;
        this.packetService = packetService;

    }

    public void createControllerAndSession(){
        ControllerConfig cf = new ControllerConfig(controllerIp);

        if (configService.findByIp(controllerIp)==null){
            configService.save(cf);
        }

        ControllerSession cs = new ControllerSession();
        sessionService.create(cs);
        sessionId = cs.getId();

    }

    public void createPackets(int numOfPackets)
    {
        for (int i = 0; i < numOfPackets; i++) {
            SdwnBasePacket dataPacket = (SdwnBasePacket) packetFactory.createDataPacket(i, 0);
            dataPacket.setSrcIp(controllerIp);
            dataPacket.setSessionId(sessionId);

            packetService.create(dataPacket);
        }
    }

}
