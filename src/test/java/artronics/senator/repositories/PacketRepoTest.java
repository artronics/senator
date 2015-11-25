package artronics.senator.repositories;

import artronics.gsdwn.model.PacketEntity;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class PacketRepoTest
{
    @Autowired
    private PacketRepo packetRepo;

//    private SenatorPacket packet;

    private PacketEntity packet;
    @Before
    @Transactional
    @Rollback(false)
    public void setUp() throws Exception
    {
//        packet = new SenatorPacket();
//        packet.setSrcShortAddress(30);
//        packet.setDstShortAddress(0);
        packet = new PacketEntity();
        packet.setSrcShortAddress(30);
        packet.setDstShortAddress(0);

        packetRepo.create(packet);
    }

    @Test
    @Transactional
    public void it_should_create_an_packet()
    {
//        SenatorPacket actPacket = packetRepo.find(packet.getId());
        PacketEntity actPacket = packetRepo.find(packet.getId());

        assertNotNull(actPacket);
        assertThat(actPacket.getSrcShortAddress(), equalTo(30));
        assertThat(actPacket.getDstShortAddress(), equalTo(0));
    }
}