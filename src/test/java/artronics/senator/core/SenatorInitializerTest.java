package artronics.senator.core;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerSession;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.helper.FakePacketFactory;
import artronics.senator.services.ControllerConfigService;
import artronics.senator.services.PacketService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class SenatorInitializerTest
{
    @Autowired
    SenatorInitializer initializer;

    @Autowired
    ControllerConfigService configService;

    @Autowired
    Controller controller;

    @Autowired
    PacketService packetService;

    ControllerConfig cnf;

    BlockingQueue<Packet> cntRxQueue;
    BlockingQueue<Packet> cntTxQueue;

    List<SdwnBasePacket> actPackets;

    FakePacketFactory packetFactory = new FakePacketFactory();
    @Before
    @Transactional
    @Rollback(value = false)
    public void setUp() throws Exception
    {
        cntRxQueue = controller.getCntRxPacketsQueue();
        cntTxQueue = controller.getCntTxPacketsQueue();
    }

    @Test
    @Transactional
    public void test_init_it_should_create_default_config_if_there_is_no_config()
    {
        initializer.init();

        cnf = configService.getLatest();

        assertNotNull(cnf);
        assertThat(cnf.getIp(), equalTo("192.168.1.1"));
    }

    @Test
    @Transactional
    public void it_should_create_a_session_associated_with_controller_config()
    {
        initializer.init();
        cnf = configService.getLatest();

        Set<ControllerSession> sessions = cnf.getControllerSessions();

        assertFalse(sessions.isEmpty());
    }

    @Test
    public void persistence_thread_should_persist_received_packets() throws InterruptedException
    {
        cntRxQueue.add(packetFactory.createReportPacket());
        initializer.start();
        Thread.sleep(200);

        actPackets = packetService.pagination(1, 1);

        assertFalse(actPackets.isEmpty());
        assertThat(actPackets.get(0).getDstShortAddress(), equalTo(0));
        assertThat(actPackets.get(0).getSrcShortAddress(), equalTo(30));
    }

    @Test
    public void test_persistence_for_multiple_persistence_in_queue() throws InterruptedException
    {
        initializer.start();
        final int num = 30;
        for (int i = 0; i < num; i++) {
            cntRxQueue.add(packetFactory.createDataPacket(0, i));
        }

        Thread.sleep(100);
        actPackets = packetService.pagination(1, 100);

        assertThat(actPackets.size(), equalTo(num));
    }
}