package artronics.senator.repositories.jpa;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.repositories.ControllerConfigRepo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class JpaControllerConfigRepo implements ControllerConfigRepo
{
    @PersistenceContext
    EntityManager em;

    @Override
    public ControllerConfig create(ControllerConfig controller)
    {
        em.persist(controller);

        return controller;
    }

    @Override
    public ControllerConfig update(ControllerConfig newConfig)
    {
        return em.merge(newConfig);
    }

    @Override
    public ControllerConfig find(String ip)
    {
        return em.find(ControllerConfig.class, ip);
    }

    @Override
    public ControllerConfig getLatest()
    {
        final CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
        final CriteriaQuery cq = criteriaBuilder.createQuery(ControllerConfig.class);

        final Root cntCnfTbl = cq.from(ControllerConfig.class);


        cq.orderBy(
                criteriaBuilder.desc(cntCnfTbl.get("created")),
                criteriaBuilder.desc(cntCnfTbl.get("updated")));

        Query query = em.createQuery(cq);
        query.setMaxResults(1);

        //if there is no result return null instead of exp
        ControllerConfig singleResult = null;
        try {
            singleResult = (ControllerConfig) query.getSingleResult();

        }catch (NoResultException e) {//nothing just return null
        }

        return singleResult;
    }
}
