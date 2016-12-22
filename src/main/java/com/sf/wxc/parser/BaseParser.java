package com.sf.wxc.parser;

import java.util.List;

/**
 * Created by Su Feng on 2016/12/22.
 */
public interface BaseParser {
    public List<?> parseListPage(String source, String hql);
    public Object parseContentPage(String source, String hql, Object obj);
}
