package com.sf.wxc.repository.db.feeddb;

import com.sf.wxc.beans.FeedArticle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by Su Feng on 2016/12/22.
 */
@Repository
public interface FeedArticleDbRepository extends JpaRepository<FeedArticle, Integer> {

    @Query("select  CASE  WHEN count(a)> 0 THEN true ELSE false END from FeedArticle a where a.url=:url")
    public boolean existsUrl(@Param("url") String url);

    @Query("select a from FeedArticle a where a.id>:id order by a.id limit :limit")
    public List<FeedArticle> queryByMinID(@Param("id") Integer id,@Param("limit") Integer limit);
}
