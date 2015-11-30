package artronics.senator.repositories;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.gsdwn.packet.SdwnDataPacket;
import artronics.gsdwn.packet.SdwnReportPacket;
import artronics.senator.helper.FakePacketFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class PacketRepoTest
{
    @Autowired
    private PacketRepo packetRepo;

    private FakePacketFactory packetFactory = new FakePacketFactory();
    private SdwnBasePacket reportPck;
    private SdwnBasePacket packet;

    @Before
    @Transactional
    @Rollback(false)
    public void setUp() throws Exception
    {
        packet = (SdwnBasePacket) packetFactory.createDataPacket();
        packet.setSessionId(1L);
        packet.setControllerIp("192.168.1.2");
    }

    @Test
    @Transactional
    public void it_should_create_packet()
    {
        packetRepo.create(packet);

        SdwnBasePacket actPacket = packetRepo.find(packet.getId());

        assertNotNull(actPacket);
    }

    @Test
    @Transactional
    public void it_should_create_packet_with_same_session_and_diff_controllerIp()
    {
        packetRepo.create(packet);
        SdwnBasePacket packet2 = (SdwnBasePacket) packetFactory.createDataPacket(33, 9);
        packet2.setSessionId(1L);
        packet2.setControllerIp("10.1.1.1");
        packetRepo.create(packet2);

        SdwnBasePacket actPacket = packetRepo.find(packet.getId());
        SdwnBasePacket actPacket2 = packetRepo.find(packet2.getId());

        assertNotNull(actPacket);
        assertNotNull(actPacket2);
    }

    @Test
    @Transactional
    public void test_all_header_fields()
    {
        packetRepo.create(packet);

        SdwnBasePacket actPacket = packetRepo.find(packet.getId());

        //first lets assert packet content. hibernate return CollectionBag
        //we have to convert it array list to compare.
        assertEquals(
                new ArrayList<>(actPacket.getContent()),
                packetFactory.createRawDataPacket());

        assertThat(actPacket.getSrcShortAddress(), equalTo(30));
        assertThat(actPacket.getDstShortAddress(), equalTo(0));
        assertThat(actPacket.getNetId(), equalTo(1));
        assertThat(actPacket.getNextHop(), equalTo(0));
        assertThat(actPacket.getTtl(), equalTo(20));

        assertThat(SdwnBasePacket.getSequence(), not(0L));
        assertNotNull(actPacket.getReceivedAt());
    }

    @Test
    @Transactional
    public void it_should_create_a_data_packet()
    {
        SdwnBasePacket dataPck = (SdwnBasePacket) packetFactory.createDataPacket();
        dataPck.setControllerIp("1.1.1.1");
        dataPck.setSessionId(2L);
        packetRepo.create(dataPck);

        SdwnBasePacket actPacket = packetRepo.find(dataPck.getId());

        assertThat(actPacket, instanceOf(SdwnDataPacket.class));
    }

    @Test
    @Transactional
    public void it_should_create_a_report_Packet()
    {
        SdwnBasePacket reportPck = (SdwnBasePacket) packetFactory.createReportPacket();
        reportPck.setControllerIp("2.2.2.2");
        reportPck.setSessionId(3L);
        packetRepo.create(reportPck);

        SdwnBasePacket actPacket = packetRepo.find(reportPck.getId());

        assertThat(actPacket, instanceOf(SdwnReportPacket.class));
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
            dataPacket.setControllerIp("3.3.3.3");
            dataPacket.setSessionId(10L);
            packetRepo.create(dataPacket);
        }
    }
}