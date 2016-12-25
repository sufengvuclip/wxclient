package com.sf.wxc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sf.wxc.beans.FeedArticle;
import com.sf.wxc.constant.DrupalConstant;
import com.sf.wxc.repository.db.bigdatawaydb.EntityDao;
import com.sf.wxc.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-25.
 */
@Component
public class BigDataWay {
    @Autowired
    EntityDao entityDao;
    private static ObjectMapper objectMapper = new ObjectMapper();
    public String postArticle(FeedArticle article){
        String ret = null;
        String body = buildArticlePostBody(article);
        Map<String,Object> headers = new HashMap<>();
        headers.put("Authorization","Basic YmlnZGF0YXdheTptZWl5b3VtaW1hbWE=");
        headers.put("Content-Type","application/hal+json");
        try {
            String response = HttpClientUtil.httpPostRequest(DrupalConstant.articlePostUrl,body,headers,false);
            JSONObject root = new JSONObject(response);
            ret = root.getJSONArray("nid").getJSONObject(0).getString("value");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public String postTag(String tag){
        if(StringUtils.trimToNull(tag)==null) return null;
        tag = tag.trim();
        String uuid = entityDao.getTagUUID(tag);
        if(uuid==null){
            uuid = addTag(tag);
        }
        return uuid;
    }


    /**
     * drupal operation on rest api
     */

    public String addTag(String tag){
        String ret = null;
        Map<String,Object> headers = new HashMap<>();
        headers.put("Authorization","Basic YmlnZGF0YXdheTptZWl5b3VtaW1hbWE=");
        headers.put("Content-Type","application/hal+json");
        String body = MessageFormat.format(DrupalConstant.tagPostContent,tag);
        try {
            String response = HttpClientUtil.httpPostRequest(DrupalConstant.tagPostUrl,body,headers,false);
            JSONObject root = new JSONObject(response);
            JSONArray arr = (JSONArray) root.get("uuid");
            ret = arr.getJSONObject(0).getString("value");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }

    public String buildArticlePostBody(FeedArticle article){
        JSONObject root = new JSONObject();

        JSONObject links = new JSONObject("{\n" +
                "    \"type\": {\n" +
                "      \"href\": \"http://www.bigdataway.net/rest/type/node/article\"\n" +
                "    }\n" +
                "  }");
        root.put("_links",links);

        JSONObject title = new JSONObject();
        title.put("value",article.getTitle());
        title.put("lang","zh-hans");
        root.put("title",title);

        JSONObject body = new JSONObject();
        title.put("value",article.getContent());
        title.put("summary",article.getDescription());
        title.put("lang","zh-hans");
        title.put("format","full_html");
        root.put("body",body);

        JSONObject _embedded = new JSONObject();
        JSONArray tags = new JSONArray();
        String articleTags = article.getTags();
        if(articleTags!=null && articleTags.length()>0){
            String[] tagArr = articleTags.split(" ");
            for(String tag: tagArr){
                if(StringUtils.trimToNull(tag)!=null){
                    String uuid = postTag(tag.trim());
                    if(uuid!=null) {
                        JSONObject tagNode = new JSONObject();
                        tagNode.put("lang", "zh-hans");
                        JSONArray uuidArr = new JSONArray("[\n" +
                                "          {\n" +
                                "            \"value\": \"" + uuid + "\"\n" +
                                "          }\n" +
                                "        ]");
                        tagNode.put("uuid",uuidArr);
                        JSONObject type = new JSONObject("{\n" +
                                "\n" +
                                "          \"type\": {\n" +
                                "            \"href\": \"http://www.bigdataway.net/rest/type/taxonomy_term/tags\"\n" +
                                "          }\n" +
                                "        }");
                        tagNode.put("_links",type);

                    }
                }
            }
        }

        _embedded.put("http://www.bigdataway.net/rest/relation/node/article/field_tags",tags);
        root.put("_embedded",_embedded);
        return root.toString();
    }
}
