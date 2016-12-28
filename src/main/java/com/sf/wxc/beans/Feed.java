package com.sf.wxc.beans;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Su Feng on 2016/12/22.
 */
@Data
@AllArgsConstructor
@Entity
@ToString
@Table(name = "feed")
public class Feed {
    @Id
    private int id;
    private String name;
    private String url;
    private String domain;
    private String listJhql;
    private String contentJhql;
    private String parserClass;
    private String entityClass;
    private Boolean contentPageRedirect;
    private String contentPagePreUrl;
    private Boolean listPageMobile;
    private Boolean contentPageMobile;
    private int frequency;
    private String tags;
    private String category;
    private String loginJson;
    private Boolean imgProxy;
    private Boolean active;

    public Feed() {
    }

    public JSONObject getLoginJsonObject(){
        if(StringUtils.trimToNull(loginJson)==null) return null;
        return new JSONObject(loginJson);
    }
}
