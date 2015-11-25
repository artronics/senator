package artronics.senator.repositories.jpa;

import artronics.senator.model.SdwnControllerModel;
import artronics.senator.repositories.ControllerRepo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class JpaControllerRepo implements ControllerRepo
{
    @PersistenceContext
    EntityManager em;

    @Override
    public SdwnControllerModel create(SdwnControllerModel controller)
    {
        em.persist(controller);

        return controller;
    }

    @Override
    public SdwnControllerModel find(Long id)
    {
        return em.find(SdwnControllerModel.class, id);
    }
}
