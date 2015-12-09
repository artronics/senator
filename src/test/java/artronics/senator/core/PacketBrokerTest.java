package artronics.senator.core;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.helper.FakePacketFactory;
import artronics.senator.services.PacketService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.concurrent.BlockingQueue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class PacketBrokerTest
{
    String thisIp = "192.168.30.1";
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
    SenatorConfig config;

    FakePacketFactory packetFactory = new FakePacketFactory();

    BlockingQueue<Packet> cntTxPackets;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        thisIp = config.getControllerIp();

        cntTxPackets = sdwnController.getCntTxPacketsQueue();

        packetBroker.start();
    }

    @Test
    public void it_should_send_a_packet_to_sdwnController_if_srcIp_equals_thisIp() throws
            InterruptedException
    {
        SdwnBasePacket packet = addPacketToBroker(thisIp);

        SdwnBasePacket actPacket = (SdwnBasePacket) cntTxPackets.take();

        FakePacketFactory.assertPacketEqual(packet, actPacket);
    }

    @Test
    public void if_srcIp_equals_this_ip_packet_should_be_persisted()
    {
        SdwnBasePacket packet = addPacketToBroker(thisIp);
    }

    private SdwnBasePacket addPacketToBroker(String srcIp)
    {
        SdwnBasePacket packet = new SdwnBasePacket(packetFactory.createRawDataPacket());
        packet.setSrcIp(srcIp);

        packetBroker.addPacket(packet);

        return packet;
    }
}