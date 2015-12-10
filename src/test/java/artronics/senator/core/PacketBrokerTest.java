package artronics.senator.core;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.helper.FakePacketFactory;
import artronics.senator.repositories.PacketRepo;
import artronics.senator.services.PacketService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.BlockingQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PacketBrokerTest
{
    String ourIp = "192.168.30.1";
    String otherIp = "192.168.120.46";

    @InjectMocks
    SenatorPacketBroker mockedPacketBroker;

    @Mock
    PacketService mockPacketService;

    @Mock
    Controller mockSdwnController;

    @Mock
    SenatorConfig mockConfig;

    @Autowired
    Controller sdwnController;

    @Autowired
    PacketBroker packetBroker;

    @Autowired
    PacketRepo packetRepo;

    @Autowired
    SenatorConfig config;

    FakePacketFactory packetFactory = new FakePacketFactory();

    BlockingQueue<Packet> cntTxPackets;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        ourIp = config.getControllerIp();

        cntTxPackets = sdwnController.getCntTxPacketsQueue();

        packetBroker.start();
    }

    @Test
    public void it_should_send_a_packet_to_sdwnController() throws
            InterruptedException
    {
        SdwnBasePacket packet = addPacketToBroker(ourIp);

        SdwnBasePacket actPacket = (SdwnBasePacket) cntTxPackets.take();

        FakePacketFactory.assertPacketEqual(packet, actPacket);
    }

    private SdwnBasePacket addPacketToBroker(String srcIp, String dstIp)
    {
        SdwnBasePacket packet = new SdwnBasePacket(packetFactory.createRawDataPacket());
        packet.setSrcIp(srcIp);
        packet.setDstIp(dstIp);
        packet.setSessionId(1L);

        packetBroker.addPacket(packet);

        return packet;
    }

    private SdwnBasePacket addPacketToBroker(String dstIp)
    {
        return addPacketToBroker(dstIp, dstIp);
    }
}