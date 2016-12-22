package com.sf.wxc.beans;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Su Feng on 2016/12/22.
 */
@Data
@Entity
@Table(name = "feed")
public class FeedArticle {
    @Id
    private int id;
    private long feedId;
    private String domain;
    private String title;
    private String url;
    private String thumbnailUrl;
    private String description;
    private String content;
    private String author;
    private String publishDate;
    private String tags;
    private String category;
}
