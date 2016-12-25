package com.sf.wxc.schedule;

import com.sf.wxc.beans.FeedArticle;
import com.sf.wxc.repository.db.feeddb.FeedArticleDbRepository;
import com.sf.wxc.service.BigDataWay;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
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
    @Autowired
    Environment env;

    @Scheduled(fixedDelay = 60 * 60 * 1000)
    public void postNewArticle() {
        List<FeedArticle> articleList = null;
        String lastidFilePath = env.getProperty("drupal.post.last.artile.id.file");
        File lastidFile = new File(lastidFilePath);
        int lastId = -1;
        do {
            try {
                lastId = NumberUtils.toInt(FileUtils.readFileToString(lastidFile,"utf-8"),-1);
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(lastId==-1) {
                logger.error("read lastidFile error {}",lastidFilePath);
                break;
            }
            articleList = feedArticleDbRepository.queryByMinID(lastId, 100);
            if (articleList != null && articleList.size() > 0) {
                for (FeedArticle article : articleList) {
                    String nodeId = bigDataWay.postArticle(article);
                    if(nodeId!=null && nodeId.length()>0) {
                        try {
                            FileUtils.writeStringToFile(lastidFile, String.valueOf(article.getId()), "utf-8");
                        } catch (Exception e) {
                            e.printStackTrace();
                            break;
                        }
                    }
                    logger.info("added new article {}, node id {}", article.getId(), nodeId);
                }
            }
        }while(articleList!=null && articleList.size()>0);

    }
}