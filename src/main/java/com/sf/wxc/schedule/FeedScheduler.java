package com.sf.wxc.schedule;

import com.sf.wxc.beans.Feed;
import com.sf.wxc.beans.FeedArticle;
import com.sf.wxc.parser.BaseParser;
import com.sf.wxc.repository.db.feeddb.FeedArticleDbRepository;
import com.sf.wxc.repository.db.feeddb.FeedDbRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Su Feng on 2016/12/22.
 */
@Component
public class FeedScheduler {
    private static Logger logger = LoggerFactory.getLogger(FeedScheduler.class);

    @Autowired
    FeedDbRepository feedDbRepository;
    @Autowired
    FeedArticleDbRepository feedArticleDbRepository;

    /**
     * every 1 hour run.
     */
    @Scheduled(fixedDelay = 120 * 60 * 1000)
    public void parseFeeds() {
        logger.info("start parse feeds....");

        Sort sort = new Sort(Sort.Direction.ASC, "id");
        List<Feed> feeds = feedDbRepository.findAll(sort);
        if (feeds != null && feeds.size() > 0) {
            for (Feed feed : feeds) {
                logger.info("parsing feed {}",feed.toString());
                Class<?> parserClazz = null;
                Class<?> entityClazz = null;
                try {
                    parserClazz = Class.forName(feed.getParserClass());
                    entityClazz = Class.forName(feed.getEntityClass());
                    BaseParser parser = (BaseParser) parserClazz.newInstance();
                    List<?> list = parser.parseListPage(feed);
                    if(list!=null && list.size()>0) {
                        for (Object o : list) {
                            if (entityClazz.equals(FeedArticle.class)) {
                                if (((FeedArticle) o).validated()) {
                                    try {
                                        feedArticleDbRepository.save((FeedArticle) o);
                                        logger.info("insert article {} {}", ((FeedArticle) o).getTitle(), ((FeedArticle) o).getUrl());
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                        logger.error("insert article error {} {}", ((FeedArticle) o).getTitle(), ((FeedArticle) o).getUrl());
                                    }
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                try {
                    //pause 2 minutes for each feed.
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
