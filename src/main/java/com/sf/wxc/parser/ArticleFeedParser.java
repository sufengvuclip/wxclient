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

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by Su Feng on 2016/12/22.
 */
public class ArticleFeedParser extends JHQLParser implements BaseParser{
    static ObjectMapper objectMapper = new ObjectMapper();
    static JavaType javaType = objectMapper.getTypeFactory().constructParametricType(List.class, FeedArticle.class);
    FeedArticleDbRepository feedArticleDbRepository = SpringUtil.getBean(FeedArticleDbRepository.class);
    @Override
    public List<FeedArticle> parseListPage(Feed feed) {
        List<FeedArticle> ret = null;

        String doc = HttpClientUtil.httpGetRequest(feed.getUrl(),feed.getListPageMobile());
        Object obj = parse(doc, feed.getListJhql());

        LinkedHashMap<String, List> map = (LinkedHashMap) obj;
        List list = map.get("articles");
        JSONArray arry = new JSONArray(list);
        try {
            ret =  (List<FeedArticle>)objectMapper.readValue(arry.toString(), javaType);
            if(ret!=null && ret.size()>0){
                for(int i=ret.size()-1;i>=0;i--){
                    FeedArticle article = ret.get(i);
                    article.transferUrl(feed);

                    if(article.validateUrlTitle() && !feedArticleDbRepository.existsUrl(article.getUrl()))
                        parseContentPage(feed, article);
                    else
                        ret.remove(i);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public FeedArticle parseContentPage(Feed feed, Object article) {
        if(StringUtils.trimToNull(feed.getContentPagePreUrl())!=null && !StringUtils.trimToEmpty(((FeedArticle) article).getUrl()).startsWith("http")){
            String articleUrl = feed.getContentPagePreUrl()+((FeedArticle) article).getUrl();
            ((FeedArticle) article).setUrl(articleUrl);
        }
        String contentUrl = ((FeedArticle) article).getUrl();
        if(feed.getContentPageRedirect()){
            contentUrl = HttpClientUtil.httpGetRedirectFinalUrl(contentUrl, feed.getContentPageMobile());
        }

        String doc = HttpClientUtil.httpGetRequest(contentUrl, feed.getContentPageMobile());
        Object obj = parse(doc, feed.getContentJhql());
        LinkedHashMap<String, Object> map = (LinkedHashMap) obj;
        ((FeedArticle) article).setDomain(feed.getDomain());
        ((FeedArticle) article).setFeedId(feed.getId());
        ((FeedArticle) article).setCategory(feed.getCategory());
        ((FeedArticle) article).setContent(map.get("content")==null?null:map.get("content").toString());
        ((FeedArticle) article).setAuthor(map.get("author")==null?null:map.get("author").toString());
        ((FeedArticle) article).setTags(map.get("tags")==null?null:map.get("tags").toString());

        return (FeedArticle) article;
    }
}
