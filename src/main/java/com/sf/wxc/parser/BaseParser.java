package com.sf.wxc.parser;

import com.sf.wxc.beans.Feed;

import java.util.List;

/**
 * Created by Su Feng on 2016/12/22.
 */
public interface BaseParser {
    public List<?> parseListPage(Feed feed);
    public Object parseContentPage(Feed feed, Object obj);
}
