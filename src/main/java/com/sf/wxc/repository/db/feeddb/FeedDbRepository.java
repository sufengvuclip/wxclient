package com.sf.wxc.repository.db.feeddb;

import com.sf.wxc.beans.Feed;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Iterator;

/**
 * Created by Su Feng on 2016/12/22.
 */
@Repository
public interface FeedDbRepository extends JpaRepository<Feed, Integer> {
    public Iterator<Feed> findByActiveTrue();
}
