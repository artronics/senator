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

import java.util.List;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

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
    public void it_should_send_a_packet_to_sdwnController_if_dstIp_equals_ourIp() throws
            InterruptedException
    {
        SdwnBasePacket packet = addPacketToBroker(ourIp);

        SdwnBasePacket actPacket = (SdwnBasePacket) cntTxPackets.take();

        FakePacketFactory.assertPacketEqual(packet, actPacket);
    }

    //This test should be run with fresh context load
    //I'm not sure if result is true
    @Test
    public void it_should_not_send_a_packet_to_sdwnController_if_dstIp_is_not_ourIp() throws
            InterruptedException
    {
        SdwnBasePacket packet = addPacketToBroker(otherIp);

        Thread.sleep(200);

        assertTrue(cntTxPackets.isEmpty());
    }

    @Test
    public void if_dstIp_equals_ourIp_packet_should_be_persisted()
    {
        SdwnBasePacket packet = addPacketToBroker(ourIp);

        List<SdwnBasePacket> persistedPackets = packetRepo.getAllPackets();

        assertThat(persistedPackets.size(), equalTo(1));

        SdwnBasePacket persistedPacket = persistedPackets.get(0);
        FakePacketFactory.assertPacketEqual(packet, persistedPacket);
    }

    @Test
    public void if_dstIp_is_not_ourIp_packet_should_not_be_persisted()
    {
        SdwnBasePacket packet = addPacketToBroker(otherIp);

        List<SdwnBasePacket> persistedPackets = packetRepo.getAllPackets();

        assertThat(persistedPackets.size(), equalTo(0));
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