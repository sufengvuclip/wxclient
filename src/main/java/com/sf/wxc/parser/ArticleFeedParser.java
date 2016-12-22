package com.sf.wxc.parser;

import com.sf.wxc.beans.FeedArticle;

import java.util.List;

/**
 * Created by Su Feng on 2016/12/22.
 */
public class ArticleFeedParser extends JHQLParser implements BaseParser{

    @Override
    public List<FeedArticle> parseListPage(String source, String hql) {
        Object obj = parse(source, hql);
        System.out.println(obj.toString());
        return null;
    }

    @Override
    public FeedArticle parseContentPage(String source, String hql, Object article) {
        article = (FeedArticle)article;
        Object obj = parse(source, hql);
        return null;
    }
}
