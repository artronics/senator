package artronics.senator.repositories;

import artronics.senator.model.SenatorPacket;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.Assert.assertTrue;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("classpath:senator-beans.xml")
public class PacketRepoTest
{
    @Autowired
    private PacketRepo packetRepo;

    private SenatorPacket packet;

    @Before
    @Transactional
    @Rollback(false)
    public void setUp() throws Exception
    {
        packet = new SenatorPacket();
        packet.setSrcShortAddress(30);
        packet.setDstShortAddress(0);

        packetRepo.create(packet);
    }

    @Test
    @Transactional
    public void it_should_create_an_account()
    {
//        SenatorPacket actPacket = packetRepo.
        assertTrue(true);
    }
}