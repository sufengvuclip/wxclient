package com.sf.wxc.beans;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Created by Su Feng on 2016/12/22.
 */
@Data
@AllArgsConstructor
@Entity
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
    private int frequency;
}
