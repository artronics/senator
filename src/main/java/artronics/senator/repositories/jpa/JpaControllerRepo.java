package artronics.senator.repositories.jpa;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.repositories.ControllerRepo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

@Repository
public class JpaControllerRepo implements ControllerRepo
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
    public ControllerConfig find(Long id)
    {
        return em.find(ControllerConfig.class, id);
    }

    @Override
    public ControllerConfig getLatest()
    {
        Query query = em.createQuery(
                "FROM artronics.gsdwn.model.ControllerConfig order by created DESC");
        query.setMaxResults(1);

        return (ControllerConfig) query.getSingleResult();
    }
}
