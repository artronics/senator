package artronics.senator.repositories.jpa;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.repositories.PacketRepo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class JpaPacketRepo implements PacketRepo
{
    @PersistenceContext
    EntityManager em;

    @Override
    public SdwnBasePacket create(SdwnBasePacket packet)
    {
        em.persist(packet);
        return packet;
    }

    @Override
    public SdwnBasePacket find(Long id)
    {
        return em.find(SdwnBasePacket.class, id);
    }
}
