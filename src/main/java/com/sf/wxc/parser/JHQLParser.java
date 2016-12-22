package com.sf.wxc.parser;

import org.github.wks.jhql.Jhql;
import org.github.wks.jhql.query.Queryer;

/**
 * Created by Su Feng on 2016/12/22.
 */
public abstract class JHQLParser {
    protected Object parse(String source, String json){
        Jhql jhql = new Jhql();
        Queryer queryer = jhql.makeQueryer(json);
        Object result = jhql.queryHtml(queryer,source);
        jhql = null;
        return result;
    }
}
