package com.sf.wxc.beans;

import lombok.Data;
import lombok.ToString;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * Created by Su Feng on 2016/12/22.
 */
@Data
@ToString
@Entity
@Table(name = "article")
public class FeedArticle {
    @Id
    private int id;
    private int feedId;
    private String domain;
    private String title;
    private String url;
    private String originalUrl;
    private String thumbnailUrl;
    private String description;
    private String content;
    private String author;
    private Date publishDate;
    private Date grabDate;
    private String tags;
    private String category;

    public FeedArticle(){
    }

    public boolean validateUrlTitle(){
        return StringUtils.trimToNull(this.title)!=null && StringUtils.trimToNull(this.url)!=null;
    }
    public boolean validated(){
        return StringUtils.trimToNull(this.title)!=null && StringUtils.trimToNull(this.url)!=null && StringUtils.trimToNull(this.content)!=null;
    }
    public void transferUrl(Feed feed){
        if(StringUtils.trimToNull(feed.getContentPagePreUrl())!=null && !StringUtils.trimToEmpty(this.url).startsWith("http")){
            this.url = feed.getContentPagePreUrl()+this.url;
        }
    }
}
