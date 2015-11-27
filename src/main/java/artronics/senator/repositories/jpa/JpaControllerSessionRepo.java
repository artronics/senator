package artronics.senator.repositories.jpa;

import artronics.gsdwn.model.ControllerSession;
import artronics.senator.repositories.ControllerSessionRepo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
