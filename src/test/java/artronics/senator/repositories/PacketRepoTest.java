package artronics.senator.repositories;

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
import static org.hamcrest.core.IsInstanceOf.instanceOf;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class PacketRepoTest
{
    @Autowired
    private PacketRepo packetRepo;

//    private PacketFactory packetFactory = new SdwnPacketFactory();

    private FakePacketFactory packetFactory = new FakePacketFactory();
    private SdwnBasePacket dataPck;
    private SdwnBasePacket reportPck;

    @Before
    @Transactional
    @Rollback(false)
    public void setUp() throws Exception
    {
        //For all packets src is 30 des is 0
        dataPck = (SdwnBasePacket) packetFactory.createDataPacket();

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

        assertThat(SdwnBasePacket.getSequence(), equalTo(1L));
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
        for (int i = 0; i < 2; i++) {
            packetRepo.create((SdwnBasePacket) packetFactory.createDataPacket(30, i));
        }

        List<SdwnBasePacket> packets = packetRepo.pagination(1, 10);

        assertThat(packets.size(), equalTo(2));
    }

    @Test
    @Transactional
    public void test_pagination_it_should_return_max_result()
    {
        final int MAX_R = 10;
        for (int i = 0; i < 20; i++) {
            packetRepo.create((SdwnBasePacket) packetFactory.createDataPacket(30, i));
        }

        List<SdwnBasePacket> packets = packetRepo.pagination(1, MAX_R);

        assertThat(packets.size(), equalTo(MAX_R));
    }

    @Test
    @Transactional
    public void test_pagination_it_should_return_latest()
    {
        final int MAX_R = 10;
        for (int i = 0; i < 50; i++) {
            packetRepo.create((SdwnBasePacket) packetFactory.createDataPacket(30, i));
        }

        List<SdwnBasePacket> packets = packetRepo.pagination(1, MAX_R);

        assertThat(packets.get(0).getDstShortAddress(), equalTo(49));
    }
}