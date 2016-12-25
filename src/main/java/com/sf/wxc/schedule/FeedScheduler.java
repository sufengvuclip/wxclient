package com.sf.wxc.schedule;

import com.sf.wxc.beans.Feed;
import com.sf.wxc.beans.FeedArticle;
import com.sf.wxc.parser.BaseParser;
import com.sf.wxc.repository.db.feeddb.FeedArticleDbRepository;
import com.sf.wxc.repository.db.feeddb.FeedDbRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Su Feng on 2016/12/22.
 */
@Component
@EnableScheduling
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
        List<Feed> feeds = feedDbRepository.findAll();
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
                    for (Object o : list) {
                        System.out.println(o.toString());
                        if(entityClazz.equals(FeedArticle.class)) {
                            if(((FeedArticle)o).validated())
                                feedArticleDbRepository.save((FeedArticle) o);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
