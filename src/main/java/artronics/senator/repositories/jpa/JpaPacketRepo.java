package artronics.senator.repositories.jpa;

import artronics.gsdwn.packet.SdwnBasePacket;
import artronics.senator.repositories.PacketRepo;
import artronics.senator.services.PacketList;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

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

    @Override
    public List<SdwnBasePacket> pagination(int pageNumber, int pageSize)
    {
        final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        final CriteriaQuery cq = criteriaBuilder.createQuery(SdwnBasePacket.class);

        final Root packetsTbl = cq.from(SdwnBasePacket.class);

        cq.orderBy(criteriaBuilder.desc(packetsTbl.get("id")));

        Query query = em.createQuery(cq);

        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);

        List<SdwnBasePacket> packets = (List<SdwnBasePacket>) query.getResultList();

        return packets;
    }

    @Override
    public PacketList getNew(Long lastPacketId, String controllerIp, Long sessionId)
    {
        Query q = em.createQuery("from artronics.gsdwn.packet.SdwnBasePacket p where " +
                                         "p.id > ?1 " +
                                         "and p.controllerIp = ?2 " +
                                         "and p.sessionId = ?3 " +
                                         "order by p.id DESC");
        q.setParameter(1, lastPacketId);
        q.setParameter(2, controllerIp);
        q.setParameter(3, sessionId);

        List<SdwnBasePacket> packets = (List<SdwnBasePacket>) q.getResultList();
        PacketList packetList = new PacketList(packets);

        return packetList;
    }

    //TODO find a way to reuse general getNew method
    @Override
    public PacketList getNew(Long lastPacketId)
    {
        Query q = em.createQuery("from artronics.gsdwn.packet.SdwnBasePacket p where " +
                                         "p.id > ?1 " +
                                         "order by p.id DESC");
        q.setParameter(1, lastPacketId);

        List<SdwnBasePacket> packets = (List<SdwnBasePacket>) q.getResultList();
        PacketList packetList = new PacketList(packets);

        return packetList;
    }
}
