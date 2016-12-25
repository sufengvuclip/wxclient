package com.sf.wxc.schedule;

import com.sf.wxc.beans.FeedArticle;
import com.sf.wxc.repository.db.feeddb.FeedArticleDbRepository;
import com.sf.wxc.service.BigDataWay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Administrator on 2016-12-25.
 */
@Component
public class BigDataWayScheduler {
    private static Logger logger = LoggerFactory.getLogger(BigDataWayScheduler.class);
    @Autowired
    FeedArticleDbRepository feedArticleDbRepository;
    @Autowired
    BigDataWay bigDataWay;

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void postNewArticle() {
        List<FeedArticle> articleList = feedArticleDbRepository.findAll();
        if(articleList!=null && articleList.size()>0){
            for(FeedArticle article:articleList){
                String nodeId = bigDataWay.postArticle(article);
                logger.info("added new article, node id {}",nodeId);
            }
        }

    }
}
