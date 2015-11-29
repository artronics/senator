package artronics.senator.repositories.jpa;

import artronics.gsdwn.model.ControllerSession;
import artronics.senator.repositories.ControllerSessionRepo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class JpaControllerSessionRepo implements ControllerSessionRepo
{
    @PersistenceContext
    EntityManager em;

    @Override
    public ControllerSession create(ControllerSession controllerSession)
    {
        em.persist(controllerSession);
        return controllerSession;
    }

    @Override
    public ControllerSession find(Long id)
    {
        return em.find(ControllerSession.class, id);
    }

    @Override
    public List<ControllerSession> pagination(int pageNumber, int pageSize)
    {
        final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        final CriteriaQuery cq = criteriaBuilder.createQuery(ControllerSession.class);

        final Root sessionsTbl = cq.from(ControllerSession.class);

        cq.orderBy(criteriaBuilder.desc(sessionsTbl.get("created")));

        Query query = em.createQuery(cq);

        query.setFirstResult((pageNumber - 1) * pageSize);
        query.setMaxResults(pageSize);

        List<ControllerSession> sessions = (List<ControllerSession>) query.getResultList();

        return sessions;
    }
}
