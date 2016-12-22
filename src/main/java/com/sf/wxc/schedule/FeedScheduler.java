package com.sf.wxc.schedule;

import com.sf.wxc.beans.Feed;
import com.sf.wxc.parser.BaseParser;
import com.sf.wxc.repository.db.feeddb.FeedArticleDbRepository;
import com.sf.wxc.repository.db.feeddb.FeedDbRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by Su Feng on 2016/12/22.
 */
@Component
public class FeedScheduler {

    @Autowired
    FeedDbRepository feedDbRepository;
    @Autowired
    FeedArticleDbRepository feedArticleDbRepository;
    /**
     * every 1 hour run.
     */
    @Scheduled(fixedDelay = 60*60*1000)
    public void parseFeeds() {
        List<Feed> feeds = feedDbRepository.findAll();
        if(feeds!=null && feeds.size()>0){
            for(Feed feed: feeds){
                Class<?> clazz = null;
                try {
                    clazz = Class.forName(feed.getParserClass());
                    BaseParser parser = (BaseParser) clazz.newInstance();

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public static void main(String[] args) {

    }
}
