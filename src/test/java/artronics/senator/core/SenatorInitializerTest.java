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
        cnf = configService.find("192.168.1.1");

        //TODO work around getControllerSessions by calling sessionRepo. find the problem
//        List<ControllerSession> sessions = cnf.getControllerSessions();
        List<ControllerSession> sessions = sessionRepo.findByControllerIp("192.168.1.1", 1, 10);

        assertThat(sessions.size(), equalTo(1));
    }

    @Test
    @Transactional
    public void if_there_is_already_a_controller_in_db_it_should_create_a_new_session_for_that()
    {
        //given there is already a cont in db
        cnf = new ControllerConfig("192.168.2.2");
        configService.create(cnf);
        //and there is a session associated with that
        ControllerSession cs = new ControllerSession();
        cs.setDescription("for 1st");
//        cs.setControllerConfig(cnf);
        sessionRepo.create(cs);

        //when
        initializer.init();
        List<ControllerSession> sessions = sessionRepo.findByControllerIp("192.168.2.2", 1, 10);

        //size should be 2 because for each run it should create a new session
        assertThat(sessions.size(), equalTo(2));
    }

    //    SdwnBasePacket packet= null ;
    @Test
    @Transactional
    public void packet_service_should_persist_packets()
    {
        //Given
        ControllerConfig cnf = new ControllerConfig("1.1.1.1");
        configService.create(cnf);
        //and ControllerSession has a ManyToOne rel with ControllerConfig
        ControllerSession cs = new ControllerSession();
//        cs.setControllerConfig(cnf);
        sessionService.create(cs);

        //When
        SdwnBasePacket packet = (SdwnBasePacket) packetFactory.createDataPacket();
//        packet.setControllerSession(cs); //Here I add ControllerSession to Packet
        packetService.create(packet);

        SdwnBasePacket actPacket = packetService.find(packet.getId());
        assertNotNull(actPacket);
    }

    @Test
    @Transactional
    public void test_calling_packetService_in_a_thread() throws InterruptedException
    {
        ControllerConfig cnf = new ControllerConfig("1.1.1.1");
        configService.create(cnf);

        final ControllerSession cs = new ControllerSession();
//        cs = new ControllerSession();
//        cs.setControllerConfig(cnf);
        sessionService.create(cs);

        BlockingQueue<Packet> queue = new LinkedBlockingQueue<>();
        queue.add(packetFactory.createDataPacket());

        Thread thr = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try {
//                    packet =(SdwnBasePacket) queue.take();
                    final SdwnBasePacket packet = (SdwnBasePacket) queue.take();
//                    SdwnBasePacket packet = new SdwnBasePacket(packetFactory
// .createRawDataPacket());
                    packetService.create(packet);
                }catch (InterruptedException e) {
                }
            }
        });
        thr.start();

        Thread.sleep(200);
//        packet.setControllerSession(cs);
//        packetService.create(packet);
    }

    @Test
    @Transactional
    public void call_service_in_an_inner_Runnable_class() throws InterruptedException
    {
        ControllerConfig cnf = new ControllerConfig("1.1.1.1");
        configService.create(cnf);

        ControllerSession cs = new ControllerSession();
//        cs.setControllerConfig(cnf);
        sessionService.create(cs);
        BlockingQueue<Packet> queue = new LinkedBlockingQueue<>();

        Thread thr = new Thread(new PacketPersistence(cs, packetService, queue));
        thr.start();

        queue.add(packetFactory.createDataPacket());

        Thread.sleep(200);
    }

    @Test
    @Transactional
    public void persistence_thread_should_persist_received_packets() throws InterruptedException
    {
        initializer.init();
        initializer.start();
        cntRxQueue.add(packetFactory.createDataPacket());
        Thread.sleep(500);

        actPackets = packetService.pagination(1, 1);

        assertFalse(actPackets.isEmpty());
        assertThat(actPackets.get(0).getDstShortAddress(), equalTo(0));
        assertThat(actPackets.get(0).getSrcShortAddress(), equalTo(30));
    }

    @Test
    @Transactional
    public void test_persistence_for_multiple_persistence_in_queue() throws InterruptedException
    {
        initializer.init();
        initializer.start();
        final int num = 30;
        for (int i = 0; i < num; i++) {
            cntRxQueue.add(packetFactory.createDataPacket(0, i));
        }

        Thread.sleep(100);
        actPackets = packetService.pagination(1, 100);

        assertThat(actPackets.size(), equalTo(num));
    }

    class PacketPersistence implements Runnable
    {
        ControllerSession controllerSession;
        PacketService packetService;
        BlockingQueue<Packet> rxQueue;

        public PacketPersistence(ControllerSession controllerSession,
                                 PacketService packetService,
                                 BlockingQueue<Packet> rxQueue)
        {
            this.controllerSession = controllerSession;
            this.packetService = packetService;
            this.rxQueue = rxQueue;
        }

        @Override
        public void run()
        {
            try {
                final SdwnBasePacket packet = (SdwnBasePacket) rxQueue.take();
//                packet.setControllerSession(controllerSession);
                packetService.create(packet);
            }catch (InterruptedException e) {
            }
        }
    }
}