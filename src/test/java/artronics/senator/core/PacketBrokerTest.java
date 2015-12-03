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
    static final String THIS_IP = "192.1.1.1";

    @InjectMocks
    SenatorPacketBroker packetBroker;

    @Mock
    PacketService packetService;

    @Mock
    Controller mockSdwnController;

    @Autowired
    Controller sdwnController;

    FakePacketFactory packetFactory = new FakePacketFactory();

    BlockingQueue<Packet> cntTxPackets;

    @Before
    public void setUp() throws Exception
    {
        MockitoAnnotations.initMocks(this);

        cntTxPackets = sdwnController.getCntTxPacketsQueue();
    }

    @Test
    public void it_should_send_a_packet_to_sdwnController() throws InterruptedException
    {
        SdwnBasePacket packet = new SdwnBasePacket(packetFactory.createRawDataPacket());
        packet.setControllerIp(THIS_IP);

        SdwnBasePacket actPacket = (SdwnBasePacket) cntTxPackets.take();

        FakePacketFactory.assertPacketEqual(packet, actPacket);
    }

}