package artronics.senator.core;

import artronics.gsdwn.controller.Controller;
import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.packet.Packet;
import artronics.senator.helper.FakePacketFactory;
import artronics.senator.services.ControllerConfigService;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.BlockingQueue;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

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

    ControllerConfig cnf;

    BlockingQueue<Packet> cntRxQueue;
    BlockingQueue<Packet> cntTxQueue;

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
    public void persistence_thread_should_persist_received_packets() throws InterruptedException
    {
        cntRxQueue.add(packetFactory.createReportPacket());
        Thread.sleep(200);


    }
}