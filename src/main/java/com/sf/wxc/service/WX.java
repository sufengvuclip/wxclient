package com.sf.wxc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.sf.wxc.beans.Article;
import com.sf.wxc.beans.WXApp;
import com.sf.wxc.util.HttpClientUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-17.
 */
@Component
public class WX {


    public static String uploadimg_req = "https://api.weixin.qq.com/cgi-bin/media/uploadimg?access_token={0}";//文章內圖片無限制
    public static String uploadmaterial_req = "https://api.weixin.qq.com/cgi-bin/material/add_material?access_token={0}&type={1}";//永久素材最多5000
    public static String uploadnews_req = "https://api.weixin.qq.com/cgi-bin/media/uploadnews?access_token={0}";


    public static String uploadnews(List<Article> articles, WXApp wxApp){
        if(articles==null || articles.size()==0) return null;
        String ret = null;
        String uploadnews_url = MessageFormat.format(uploadnews_req,wxApp.getToken());

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode root = objectMapper.createObjectNode ();
        ArrayNode arrayNode = objectMapper.createArrayNode();
        for(Article article: articles){
            arrayNode.add(article.toObjectNode());
        }
        root.put("articles", arrayNode);
        System.out.println("post news: "+root.toString());

        try {
            ret= HttpClientUtil.httpPostRequest(uploadnews_url,root.toString());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String uploadimg(String filepath, WXApp wxApp){
        String ret = null;
        String uploadimg_url = MessageFormat.format(uploadimg_req,wxApp.getToken());
        Map<String,String> map = new HashMap<>();
        String filename = FilenameUtils.getName(filepath);
        map.put("filename",filename);
        System.out.println("filename: "+filename);
        //map.put("filelength",filename);

        String response = HttpClientUtil.uploadFile(uploadimg_url,filepath,map);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map jsonmap = objectMapper.readValue(response, Map.class);
            ret = (String) jsonmap.get("url");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    public static String uploadmaterial(String filepath, WXApp wxApp, String type){
        String ret = null;
        String uploadmaterial_url = MessageFormat.format(uploadmaterial_req,wxApp.getToken(),type);
        Map<String,String> map = new HashMap<>();
        String filename = FilenameUtils.getName(filepath);
        map.put("filename",filename);
        String response = HttpClientUtil.uploadFile(uploadmaterial_url,filepath,map);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map jsonmap = objectMapper.readValue(response, Map.class);
            ret = (String) jsonmap.get("media_id");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
