package artronics.senator.repositories.jpa;

import artronics.gsdwn.model.ControllerSession;
import artronics.gsdwn.packet.SdwnBasePacket;
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
public class JpaPacketRepo implements PacketRepo
{
    @PersistenceContext
    EntityManager em;

    private ControllerSession controllerSession;

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

    public void setControllerSession(ControllerSession controllerSession)
    {
        this.controllerSession = controllerSession;
    }
}
