package com.sf.wxc.repository.db.feeddb;

import com.sf.wxc.repository.db.CommonDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
@Repository
public class FeeddbCommonDao extends CommonDao {

    @PersistenceContext(name = "feedDbEntityManager", unitName = "feedDb")
    EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
