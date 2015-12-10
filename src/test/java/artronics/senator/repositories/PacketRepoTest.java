package artronics.senator.repositories;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.gsdwn.packet.SdwnDataPacket;
import artronics.gsdwn.packet.SdwnReportPacket;
import artronics.senator.config.TestRepositoryConfig;
import artronics.senator.helper.FakePacketFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
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
@SpringApplicationConfiguration(classes = {TestRepositoryConfig.class})
public class PacketRepoTest
{
    @Autowired
    private PacketRepo packetRepo;

    private FakePacketFactory packetFactory = new FakePacketFactory();
    private SdwnBasePacket reportPck;
    private SdwnBasePacket packet;

    @Before
    public void setUp() throws Exception
    {
        packet = (SdwnBasePacket) packetFactory.createDataPacket();
        packet.setSessionId(1L);
        packet.setSrcIp("192.168.1.2");
        packet.setDstIp("192.20.14.2");
    }

    @Test
    public void it_should_create_packet()
    {
        packetRepo.save(packet);

        SdwnBasePacket actPacket = packetRepo.findOne(packet.getId());

        assertNotNull(actPacket);
    }

    @Test
    public void it_should_create_packet_with_same_session_and_diff_IPs()
    {
        packetRepo.save(packet);
        SdwnBasePacket packet2 = (SdwnBasePacket) packetFactory.createDataPacket(33, 9);
        packet2.setSessionId(1L);
        packet2.setSrcIp("10.1.1.1");
        packet2.setDstIp("12.222.12.22");
        packetRepo.save(packet2);

        SdwnBasePacket actPacket = packetRepo.findOne(packet.getId());
        SdwnBasePacket actPacket2 = packetRepo.findOne(packet2.getId());

        assertNotNull(actPacket);
        assertNotNull(actPacket2);
    }

    @Test
    public void test_all_header_fields()
    {
        packetRepo.save(packet);

        SdwnBasePacket actPacket = packetRepo.findOne(packet.getId());

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
    public void it_should_create_a_data_packet()
    {
        SdwnBasePacket dataPck = (SdwnBasePacket) packetFactory.createDataPacket();
        dataPck.setSrcIp("1.1.1.1");
        dataPck.setDstIp("1.1.1.1");
        dataPck.setSessionId(2L);
        packetRepo.save(dataPck);

        SdwnBasePacket actPacket = packetRepo.findOne(dataPck.getId());

        assertThat(actPacket, instanceOf(SdwnDataPacket.class));
    }

    @Test
    public void it_should_create_a_report_Packet()
    {
        SdwnBasePacket reportPck = (SdwnBasePacket) packetFactory.createReportPacket();
        reportPck.setSrcIp("2.2.2.2");
        reportPck.setDstIp("2.2.2.2");
        reportPck.setSessionId(3L);
        packetRepo.save(reportPck);

        SdwnBasePacket actPacket = packetRepo.findOne(reportPck.getId());

        assertThat(actPacket, instanceOf(SdwnReportPacket.class));
    }

    @Test
    public void test_getNew()
    {
        packetRepo.deleteAll();
        //first create 10 packets
        List<SdwnBasePacket> allPackets = createDataPackets(10);
        List<SdwnBasePacket> actPackets = packetRepo.getNew(5L, "3.3.3.3", 10L);

        for (int i = 0; i < actPackets.size(); i++) {
            FakePacketFactory.assertPacketEqual(allPackets.get(9 - i),
                                                actPackets.get(i));
        }
    }

    @Test
    public void getNew_should_empty_list_if_there_is_no_match()
    {
        List<SdwnBasePacket> packets = packetRepo.getNew(100L, "3.3.3.3", 10L);

        assertThat(packets.size(), equalTo(0));
    }

    @Test
    public void getNew_should_return_FROM_specified_id_PLUS_ONE_till_last_one()
    {
        packetRepo.deleteAll();
        List<SdwnBasePacket> allPck = createDataPackets(10);

        List<SdwnBasePacket> actPck = packetRepo.getNew(5L, "3.3.3.3", 10L);
        long newestId = allPck.get(9).getId();
        //plus one prove retrieved id starts form next(previous) id
        long firstId = newestId - actPck.size() + 1;

        List<SdwnBasePacket> actPackets = actPck;
        assertThat(actPck.get(0).getId(), equalTo(newestId));
        assertThat(actPackets.get(actPackets.size() - 1).getId(), equalTo(firstId));
    }

    @Test
    public void getNow_should_compare_ip_and_sessionId()
    {
        //we create exp packets among other packets with diff ip and sessionId
        List<SdwnBasePacket> otherPck0 = createDataPackets(3, "3.3.3.3", 3L);
        List<SdwnBasePacket> expPckt1 = createDataPackets(3);
        List<SdwnBasePacket> otherPck1 = createDataPackets(3, "3.3.3.3", 3L);
        List<SdwnBasePacket> otherPck2 = createDataPackets(3, "2.2.2.2", 10L);
        List<SdwnBasePacket> otherPck3 = createDataPackets(3, "2.2.2.2", 3L);
        List<SdwnBasePacket> expPckt2 = createDataPackets(4);

        List<SdwnBasePacket> expPackets = new ArrayList<>(expPckt1);
        expPackets.addAll(expPckt2);

        List<SdwnBasePacket> actPackets = packetRepo.getNew(1L, "3.3.3.3", 10L);

        assertThat(actPackets.size(), equalTo(7));
        for (int i = 0; i < 5; i++) {
            FakePacketFactory.assertPacketEqual(expPackets.get(6 - i),
                                                actPackets.get(i));
        }
    }

    @Test
    public void test_getNew_without_ip_and_sessionId()
    {
        packetRepo.deleteAll();
        List<SdwnBasePacket> expPck1 = createDataPackets(3);
        List<SdwnBasePacket> expPck2 = createDataPackets(2, "5.5.5.5", 3L);
        List<SdwnBasePacket> expPck3 = createDataPackets(4, "1.1.1.1", 10L);
        List<SdwnBasePacket> expPck4 = createDataPackets(1, "3.3.3.3", 2L);
        List<SdwnBasePacket> expPackets = new ArrayList<>(expPck1);
        expPackets.addAll(expPck2);
        expPackets.addAll(expPck3);
        expPackets.addAll(expPck4);

        List<SdwnBasePacket> actPackets = packetRepo.getNew(0L);

        assertThat(actPackets.size(), equalTo(10));
        for (int i = 0; i < 9; i++) {
            FakePacketFactory.assertPacketEqual(expPackets.get(9 - i),
                                                actPackets.get(i));
        }
    }

    private List<SdwnBasePacket> createDataPackets(int num, String ctrlIp, Long sessionId)
    {
        List<SdwnBasePacket> packets = new ArrayList<>();
        for (int i = 0; i < num; i++) {
            SdwnBasePacket dataPacket = (SdwnBasePacket) packetFactory.createDataPacket(30, i);
            dataPacket.setSrcIp(ctrlIp);
            dataPacket.setDstIp(ctrlIp);
            dataPacket.setSessionId(sessionId);
            packets.add(packetRepo.save(dataPacket));
        }

        return packets;
    }

    private List<SdwnBasePacket> createDataPackets(int num)
    {
        return createDataPackets(num, "3.3.3.3", 10L);
    }
}