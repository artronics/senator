package artronics.senator.repositories.jpa;

import artronics.senator.model.SenatorPacket;
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
    public SenatorPacket create(SenatorPacket packet)
    {
        em.persist(packet);
        return packet;
    }

    @Override
    public SenatorPacket find(Long id)
    {
        return em.find(SenatorPacket.class, id);
    }
}
