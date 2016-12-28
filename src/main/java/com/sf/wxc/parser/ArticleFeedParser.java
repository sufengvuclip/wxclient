package com.sf.wxc.parser;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sf.wxc.beans.Feed;
import com.sf.wxc.beans.FeedArticle;
import com.sf.wxc.repository.db.feeddb.FeedArticleDbRepository;
import com.sf.wxc.util.HttpClientUtil;
import com.sf.wxc.util.SpringUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Su Feng on 2016/12/22.
 */
public class ArticleFeedParser extends JHQLParser implements BaseParser{
    private static Logger logger = LoggerFactory.getLogger(ArticleFeedParser.class);
    static ObjectMapper objectMapper = new ObjectMapper();
    static JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, FeedArticle.class);
    FeedArticleDbRepository feedArticleDbRepository = SpringUtil.getBean(FeedArticleDbRepository.class);
    static final int summaryLength = 400;
    @Override
    public List<FeedArticle> parseListPage(Feed feed) {
        List<FeedArticle> ret = null;

        String doc = HttpClientUtil.httpGetRequest(feed.getUrl(),feed.getListPageMobile(),feed.getLoginJsonObject());
        Object obj = parse(doc, feed.getListJhql());

        LinkedHashMap<String, List> map = (LinkedHashMap) obj;
        List list = map.get("articles");
        if(list==null || list.size()==0){
            logger.error("####Feed parsing no result {} {}",feed.getId(),feed.getUrl());
            return null;
        }
        JSONArray arry = new JSONArray(list);
        try {
            ret =  (List<FeedArticle>)objectMapper.readValue(arry.toString(), javaType);
            if(ret!=null && ret.size()>0){
                for(int i=ret.size()-1;i>=0;i--){
                    FeedArticle article = ret.get(i);
                    article.transferUrl(feed);
                    System.out.println(article.getUrl());
                    if(article.validateUrlTitle() && !feedArticleDbRepository.existsUrl(article.getUrl())) {
                        logger.info("   parseListPage new article {} {}",article.getTitle(),article.getUrl());
                        parseContentPage(feed, article);
                    }else {
                        logger.info("   parseListPage old article {} {}",article.getTitle(),article.getUrl());
                        ret.remove(i);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            logger.error("####parse list page error {}",feed.getUrl());
        }
        return ret;
    }

    @Override
    public FeedArticle parseContentPage(Feed feed, Object article) {
        String contentUrl = ((FeedArticle) article).getUrl();
        if(feed.getContentPageRedirect()){
            contentUrl = HttpClientUtil.httpGetRedirectFinalUrl(contentUrl, feed.getContentPageMobile(),feed.getLoginJsonObject());
        }

        String doc = HttpClientUtil.httpGetRequest(contentUrl, feed.getContentPageMobile(),feed.getLoginJsonObject());
        logger.info("=================================================");
        logger.info("url {} content {}",contentUrl,doc);
        logger.info("=================================================");
        Object obj = parse(doc, feed.getContentJhql());
        LinkedHashMap<String, Object> map = (LinkedHashMap) obj;
        ((FeedArticle) article).setDomain(feed.getDomain());
        ((FeedArticle) article).setFeedId(feed.getId());
        ((FeedArticle) article).setCategory(feed.getCategory());
        ((FeedArticle) article).setContent(map.get("content")==null?null:map.get("content").toString());
        ((FeedArticle) article).setAuthor(map.get("author")==null?null:map.get("author").toString());
        ((FeedArticle) article).setTags(map.get("tags")==null?null:map.get("tags").toString());
        ((FeedArticle) article).setOriginalUrl(map.get("originalUrl")==null?null:map.get("originalUrl").toString());

        //set summary
        if(StringUtils.trimToNull(((FeedArticle) article).getDescription())==null){
            ((FeedArticle) article).setDescription(generateSummary(((FeedArticle) article).getContent(),summaryLength));
        }


        //replace the img source with proxy in content
        if(feed.getImgProxy()) {
            try {
                String content = ((FeedArticle) article).getContent();
                Document root = Jsoup.parse(content);
                Elements imgs = root.getElementsByTag("img");
                if (imgs != null && imgs.size() > 0) {
                    for (int i = 0; i < imgs.size(); i++) {
                        Element img = imgs.get(i);
                        if (img.attr("src").contains(feed.getDomain())) {
                            try {
                                img.attr("src", "/img.php?i=" + URLEncoder.encode(img.attr("src"), "utf-8"));
                            } catch (UnsupportedEncodingException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                ((FeedArticle) article).setContent(root.body().children().toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return (FeedArticle) article;
    }

    public String generateSummary(String content, int length){
        Document doc = Jsoup.parse(content,"utf-8");
        String text = doc.body().text();
        int index = Math.min(length,text.length());
        return text.substring(0,index);
    }
}
