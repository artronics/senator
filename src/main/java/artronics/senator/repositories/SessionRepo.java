package artronics.senator.repositories;

import artronics.gsdwn.model.Session;

public interface SessionRepo
{
    Session create(Session session);

    Session find (Long id);
}
