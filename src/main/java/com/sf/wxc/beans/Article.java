package com.sf.wxc.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * Created by Administrator on 2016-12-17.
 */
@Data
public class Article {
    private static ObjectMapper mapper = new ObjectMapper();
    private String title;
    private String content;
    private String digest;
    private String thumb_media_id;
    private String content_source_url;
    private String author;
    private String show_cover_pic;

    @JsonIgnore
    private String original_thumbnail_url;
    @JsonIgnore
    private String original_author;

    public ObjectNode toObjectNode(){
        ObjectNode node = mapper.createObjectNode();
        node.put("title",title);
        node.put("content",content);
        node.put("digest", StringUtils.trimToEmpty(digest));
        node.put("thumb_media_id",thumb_media_id);
        node.put("content_source_url",StringUtils.trimToEmpty(content_source_url));
        node.put("author",StringUtils.trimToEmpty(author));
        node.put("show_cover_pic",show_cover_pic);
        return node;
    }
}
/**
 *
 参数	是否必须	说明
 Articles	是	图文消息，一个图文消息支持1到10条图文
 thumb_media_id	是	图文消息缩略图的media_id，可以在基础支持-上传多媒体文件接口中获得
 author	否	图文消息的作者
 title	是	图文消息的标题
 content_source_url	否	在图文消息页面点击“阅读原文”后的页面
 content	是	图文消息页面的内容，支持HTML标签。具备微信支付权限的公众号，可以使用a标签，其他公众号不能使用
 digest	否	图文消息的描述
 show_cover_pic	否	是否显示封面，1为显示，0为不显示
 */
