package com.sf.wxc.repository.db.feeddb;

import com.sf.wxc.beans.Feed;
import com.sf.wxc.beans.FeedArticle;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-26.
 */
@Repository
public class ArticleDao extends FeeddbCommonDao{
    public List<FeedArticle> queryArticles(int startId, int limit){
        String sql = "select a.* from article a where a.id>:id order by a.id asc limit :limit";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("id",startId);
        params.put("limit",limit);
        return (List<FeedArticle>) queryListEntity(sql,params,FeedArticle.class);
    }

    public List<Feed> queryFeed(boolean active){
        String sql = "select a.* from feed a where a.active=:active";
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("active",active?1:0);
        return (List<Feed>) queryListEntity(sql,params,Feed.class);
    }
}
