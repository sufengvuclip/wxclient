package com.sf.wxc.repository.db.feeddb;

import com.sf.wxc.beans.FeedArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Created by Su Feng on 2016/12/22.
 */
@Repository
public interface FeedArticleDbRepository extends JpaRepository<FeedArticle, Integer> {
}
