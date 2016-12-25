package com.sf.wxc.repository.db.bigdatawaydb;

import com.sf.wxc.repository.db.CommonDao;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

@Transactional
@Repository
public class BigDataWaydbCommonDao extends CommonDao {

    @PersistenceContext(name = "bigdatawayDbEntityManager", unitName = "bigdatawayDb")
    EntityManager entityManager;

    @Override
    protected EntityManager getEntityManager() {
        return entityManager;
    }
}
