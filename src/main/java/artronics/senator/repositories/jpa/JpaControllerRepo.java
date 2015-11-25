package artronics.senator.repositories.jpa;

import artronics.gsdwn.model.ControllerEntity;
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
    public ControllerEntity create(ControllerEntity controller)
    {
        em.persist(controller);

        return controller;
    }

    @Override
    public ControllerEntity find(Long id)
    {
        return em.find(ControllerEntity.class, id);
    }
}
