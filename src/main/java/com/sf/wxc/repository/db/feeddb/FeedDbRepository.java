package com.sf.wxc.repository.db.feeddb;

import com.sf.wxc.beans.Feed;
import org.hibernate.mapping.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Su Feng on 2016/12/22.
 */
@Repository
public interface FeedDbRepository extends JpaRepository<Feed, Integer> {
    public List findByActiveTrue();
}
