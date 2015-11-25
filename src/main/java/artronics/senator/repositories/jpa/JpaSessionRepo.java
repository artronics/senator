package artronics.senator.repositories.jpa;

import artronics.gsdwn.model.Session;
import artronics.senator.repositories.SessionRepo;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Repository
public class JpaSessionRepo implements SessionRepo
{
    @PersistenceContext
    EntityManager em;

    @Override
    public Session create(Session session)
    {
        em.persist(session);
        return session;
    }

    @Override
    public Session find(Long id)
    {
        return em.find(Session.class,id);
    }
}
