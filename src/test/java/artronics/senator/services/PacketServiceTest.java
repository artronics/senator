package artronics.senator.services;

import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerSession;
import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.helper.FakePacketFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertNotNull;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class PacketServiceTest
{
    @Autowired
    PacketService packetService;

    @Autowired
    ControllerConfigService configService;

    @Autowired
    ControllerSessionService sessionService;

    ControllerConfig cnf;
    ControllerSession cs;

    FakePacketFactory packetFactory = new FakePacketFactory();

    @Before
    @Transactional
    public void setUp() throws Exception
    {
        cnf = new ControllerConfig("10.11.12.13");
        configService.create(cnf);

        cs = new ControllerSession();
        cs.setControllerConfig(cnf);
        sessionService.create(cs);
    }

    /*
        This is an experimental test. If we have a ControllerSession and
        each time we refer to it while persisting a packets it should work.
        During SenatorInitializer test I realized that may be referring to
        same reference (without creating new one)in a one-to-many rel may cause data integrity
        violation exp.
     */
    @Test
    @Transactional
    public void it_should_reuse_a_created_session_for_all_packet()
    {
        SdwnBasePacket packet = (SdwnBasePacket) packetFactory.createDataPacket();
        packet.setControllerSession(cs);
        packetService.create(packet);

        SdwnBasePacket packet1 = (SdwnBasePacket) packetFactory.createDataPacket(33, 23);
        packet1.setControllerSession(cs);
        packetService.create(packet1);

        SdwnBasePacket actPacket = packetService.find(packet.getId());
        SdwnBasePacket actPacket1 = packetService.find(packet1.getId());

        assertNotNull(actPacket);
        assertNotNull(actPacket1);
    }
}