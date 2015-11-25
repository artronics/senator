package artronics.senator.repositories.jpa;

import artronics.gsdwn.model.PacketEntity;
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
    public PacketEntity create(PacketEntity packet)
    {
        em.persist(packet);
        return packet;
    }

    @Override
    public PacketEntity find(Long id)
    {
        return em.find(PacketEntity.class, id);
    }
}
