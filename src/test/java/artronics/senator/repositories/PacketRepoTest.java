package artronics.senator.repositories;

import artronics.gsdwn.model.ControllerConfig;
import artronics.gsdwn.model.ControllerSession;
import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.gsdwn.packet.SdwnDataPacket;
import artronics.senator.helper.FakePacketFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class PacketRepoTest
{
    @Autowired
    private PacketRepo packetRepo;

    @Autowired
    private ControllerSessionRepo sessionRepo;

    @Autowired
    private ControllerRepo controllerRepo;

    private ControllerConfig controllerConfig = new ControllerConfig("192.168.10.1");
    private ControllerSession controllerSession = new ControllerSession();

    private FakePacketFactory packetFactory = new FakePacketFactory();
    private SdwnBasePacket dataPck;
    private SdwnBasePacket reportPck;

    @Before
    @Transactional
    @Rollback(false)
    public void setUp() throws Exception
    {
        //first save the controller
        controllerRepo.create(controllerConfig);
        // session needs a controller
        controllerSession.setControllerConfig(controllerConfig);
        //For packets controllerSession nullable is false.
        //Here we should first create a dummy session and add it to all packets
        //which we want to persist.
        sessionRepo.create(controllerSession);

        //For all packets src is 30 des is 0
        dataPck = (SdwnBasePacket) packetFactory.createDataPacket();
        dataPck.setControllerSession(controllerSession);
    }

    @Test
    @Transactional
    public void it_should_create_packet()
    {
        packetRepo.create(dataPck);

        SdwnBasePacket actPacket = packetRepo.find(dataPck.getId());

        assertNotNull(actPacket);
    }

    @Test
    @Transactional
    public void test_all_header_fields()
    {
        packetRepo.create(dataPck);

        SdwnBasePacket actPacket = packetRepo.find(dataPck.getId());

        assertNotNull(actPacket);

        assertThat(actPacket.getSrcShortAddress(), equalTo(30));
        assertThat(actPacket.getDstShortAddress(), equalTo(0));

        assertThat(SdwnBasePacket.getSequence(), not(0L));
        assertNotNull(actPacket.getReceivedAt());
    }

    @Test
    @Transactional
    public void it_should_create_a_data_packet()
    {
        packetRepo.create(dataPck);

        SdwnBasePacket actPacket = packetRepo.find(dataPck.getId());

        assertThat(actPacket, instanceOf(SdwnDataPacket.class));
    }

    @Test
    @Transactional
    public void test_pagination()
    {
        createDataPackets(2);

        List<SdwnBasePacket> packets = packetRepo.pagination(1, 10);

        assertThat(packets.size(), equalTo(2));
    }

    @Test
    @Transactional
    public void test_pagination_it_should_return_max_result()
    {
        final int MAX_R = 10;
        createDataPackets(20);

        List<SdwnBasePacket> packets = packetRepo.pagination(1, MAX_R);

        assertThat(packets.size(), equalTo(MAX_R));
    }

    @Test
    @Transactional
    public void test_pagination_it_should_return_latest()
    {
        final int MAX_R = 10;
        createDataPackets(50);

        List<SdwnBasePacket> packets = packetRepo.pagination(1, MAX_R);

        assertThat(packets.get(0).getDstShortAddress(), equalTo(49));
    }

    private void createDataPackets(int num)
    {
        for (int i = 0; i < num; i++) {
            SdwnBasePacket dataPacket = (SdwnBasePacket) packetFactory.createDataPacket(30, i);
            dataPacket.setControllerSession(controllerSession);
            packetRepo.create(dataPacket);
        }
    }
}