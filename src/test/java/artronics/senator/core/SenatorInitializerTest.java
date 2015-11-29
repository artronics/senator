package artronics.senator.core;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerSession;
import artronics.gsdwn.packet.Packet;
import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.helper.FakePacketFactory;
import artronics.senator.repositories.ControllerSessionRepo;
import artronics.senator.services.ControllerConfigService;
import artronics.senator.services.ControllerSessionService;
import artronics.senator.services.PacketService;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
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

    @Autowired
    ControllerSessionRepo sessionRepo;

    @Autowired
    ControllerSessionService sessionService;

    ControllerConfig cnf;
    ControllerSession cs;

    BlockingQueue<Packet> cntRxQueue;
    BlockingQueue<Packet> cntTxQueue;

    List<SdwnBasePacket> actPackets;

    FakePacketFactory packetFactory = new FakePacketFactory();

    @Before
    @Transactional
    public void setUp() throws Exception
    {
//        cntRxQueue = controller.getCntRxPacketsQueue();
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
    public void it_should_create_a_new_session_in_init()
    {
        initializer.init();

        List<ControllerSession> sessions = sessionService.paginate(1, 10);
        assertThat(sessions.size(), equalTo(1));
    }

    @Test
    @Transactional
    public void it_should_create_a_new_session_for_each_call_to_init()
    {
        sessionService.create(new ControllerSession());
        initializer.init();

        List<ControllerSession> sessions = sessionService.paginate(1, 10);
        assertThat(sessions.size(), equalTo(2));
    }

    @Test
    @Transactional
    public void test_calling_packetService_in_a_thread() throws InterruptedException
    {
        ControllerConfig cnf = new ControllerConfig("10.11.12.13");
        ControllerSession cs = new ControllerSession();
        configService.create(cnf);
        sessionService.create(cs);
        String ip = cnf.getIp();
        Long sessionId = cs.getId();

        BlockingQueue<Packet> queue = new LinkedBlockingQueue<>();
        queue.add(packetFactory.createDataPacket());

        Thread thr = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try {
                    final SdwnBasePacket packet = (SdwnBasePacket) queue.take();
                    packet.setControllerIp(ip);
                    packet.setSessionId(sessionId);
                    packetService.create(packet);
                }catch (InterruptedException e) {
                }
            }
        });
        thr.start();
        Thread.sleep(200);

        List<SdwnBasePacket> packets = packetService.pagination(1, 10);
        assertThat(packets.size(), equalTo(1));
    }

    @Ignore
    @Test
    @Transactional
//    @Rollback(value = false)
    public void persistence_thread_should_persist_received_packets() throws InterruptedException
    {
        initializer.init();
        initializer.start();
        cntRxQueue = controller.getCntRxPacketsQueue();
        cntRxQueue.add(packetFactory.createDataPacket());
        Thread.sleep(1000);

//        SdwnBasePacket packet = packetService.find(1L);
//        assertNotNull(packet);

        List<SdwnBasePacket> actPackets = packetService.pagination(1, 10);

        assertFalse(actPackets.isEmpty());
//        assertThat(actPackets.get(0).getDstShortAddress(), equalTo(0));
//        assertThat(actPackets.get(0).getSrcShortAddress(), equalTo(30));
    }

    @Ignore
    @Test
    @Transactional
    @Rollback(value = false)
    public void test_persistence_for_multiple_persistence_in_queue() throws InterruptedException
    {
        initializer.init();
        initializer.start();
        cntRxQueue = controller.getCntRxPacketsQueue();
        final int num = 30;
        for (int i = 0; i < num; i++) {
            cntRxQueue.add(packetFactory.createDataPacket(0, i));
        }

        Thread.sleep(500);
        actPackets = packetService.pagination(1, 100);

        assertThat(actPackets.size(), equalTo(num));
    }
}