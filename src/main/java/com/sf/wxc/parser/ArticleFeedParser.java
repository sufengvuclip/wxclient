package com.sf.wxc.parser;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.sf.wxc.beans.Feed;
import com.sf.wxc.beans.FeedArticle;
import com.sf.wxc.util.HttpClientUtil;
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
    @Override
    public List<FeedArticle> parseListPage(Feed feed) {
        List<FeedArticle> ret = null;

        String doc = HttpClientUtil.httpGetRequest(feed.getUrl());
        Object obj = parse(doc, feed.getListJhql());
        System.out.println(obj.getClass().getName());
        System.out.println(obj.toString());

        LinkedHashMap<String, List> map = (LinkedHashMap) obj;
        List list = map.get("articles");
        JSONArray arry = new JSONArray(list);
        try {
            ret =  (List<FeedArticle>)objectMapper.readValue(arry.toString(), javaType);
            if(ret!=null && ret.size()>0){
                for(FeedArticle article: ret){
                    parseContentPage(feed, article);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    @Override
    public FeedArticle parseContentPage(Feed feed, Object article) {

        String doc = HttpClientUtil.httpGetRequest("http://m.toutiao.com"+((FeedArticle) article).getUrl());
        System.out.println("===="+doc);
        Object obj = parse(doc, feed.getContentJhql());
        LinkedHashMap<String, Object> map = (LinkedHashMap) obj;
        ((FeedArticle) article).setContent(map.get("content").toString());
        return null;
    }
}
