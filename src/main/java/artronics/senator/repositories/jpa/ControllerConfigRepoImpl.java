package artronics.senator.repositories.jpa;

import artronics.gsdwn.model.ControllerConfig;
import artronics.senator.repositories.ControllerConfigCustomRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

@Repository
public class ControllerConfigRepoImpl implements ControllerConfigCustomRepo
{
    @PersistenceContext
    EntityManager em;

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
