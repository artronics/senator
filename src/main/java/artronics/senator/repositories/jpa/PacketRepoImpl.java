package artronics.senator.repositories.jpa;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.repositories.PacketCustomRepo;
import artronics.senator.repositories.PacketRepo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class PacketRepoImpl implements PacketCustomRepo
{
    @PersistenceContext
    EntityManager em;

    @Override
    public List<SdwnBasePacket> getNew(Long lastPacketId, String controllerIp, Long sessionId)
    {
        Query q = em.createQuery("from artronics.gsdwn.packet.SdwnBasePacket p where " +
                                         "p.id > ?1 " +
                                         "and p.srcIp = ?2 " +
                                         "and p.sessionId = ?3 " +
                                         "order by p.id DESC");
        q.setParameter(1, lastPacketId);
        q.setParameter(2, controllerIp);
        q.setParameter(3, sessionId);

        List<SdwnBasePacket> packets = (List<SdwnBasePacket>) q.getResultList();

        return packets;
    }

    //TODO find a way to reuse general getNew method
    @Override
    public List<SdwnBasePacket> getNew(Long lastPacketId)
    {
        Query q = em.createQuery("from artronics.gsdwn.packet.SdwnBasePacket p where " +
                                         "p.id > ?1 " +
                                         "order by p.id DESC");
        q.setParameter(1, lastPacketId);

        List<SdwnBasePacket> packets = (List<SdwnBasePacket>) q.getResultList();

        return packets;
    }
}
