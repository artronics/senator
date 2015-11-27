package artronics.senator.repositories;

import artronics.gsdwn.packet.PacketFactory;
import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.gsdwn.packet.SdwnDataPacket;
import artronics.gsdwn.packet.SdwnPacketFactory;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

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

    private PacketFactory packetFactory = new SdwnPacketFactory();

    private SdwnBasePacket dataPck;
    private SdwnBasePacket reportPck;
    @Before
    @Transactional
    @Rollback(false)
    public void setUp() throws Exception
    {
        //For all packets src is 30 des is 0

        //data payload is 123
        dataPck = (SdwnBasePacket) packetFactory.create(Arrays.asList(11,
                                                                      1,
                                                                      0,
                                                                      30,
                                                                      0,
                                                                      0,
                                                                      0,
                                                                      20,
                                                                      0,
                                                                      0,
                                                                      123));


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
    }

    @Test
    @Transactional
    @Rollback(value = false)
    public void it_should_create_a_data_packet()
    {
        packetRepo.create(dataPck);

        SdwnBasePacket actPacket = packetRepo.find(dataPck.getId());

        assertThat(actPacket, instanceOf(SdwnDataPacket.class));
    }

    @Test
    @Transactional
    public void it()
    {
//        Packet inPacket =packetFactory.create(Arrays.asList(11,1,0,0,0,30,0,20,0,0,123));
//        dataPck = (PacketEntity)inPacket;
//
//        packetRepo.create(dataPck);

    }
}