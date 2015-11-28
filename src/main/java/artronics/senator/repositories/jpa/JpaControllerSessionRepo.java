package artronics.senator.repositories.jpa;

import artronics.gsdwn.model.ControllerSession;
import artronics.senator.repositories.ControllerSessionRepo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
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
    public List<ControllerSession> findByControllerIp(String controllerIp, int pageNumber,
                                                      int pageSize)
    {
        Query q = em.createQuery(
                "from artronics.gsdwn.model.ControllerSession c where c.controllerConfig.ip=?1");
        q.setParameter(1, controllerIp);
        q.setFirstResult((pageNumber - 1) * pageSize);
        q.setMaxResults(pageSize);

        return q.getResultList();
    }
}
