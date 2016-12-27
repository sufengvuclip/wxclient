package com.sf.wxc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sf.wxc.beans.FeedArticle;
import com.sf.wxc.constant.DrupalConstant;
import com.sf.wxc.repository.db.bigdatawaydb.EntityDao;
import com.sf.wxc.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-25.
 */
@Component
public class BigDataWay {
    private static Logger logger = LoggerFactory.getLogger(BigDataWay.class);
    @Autowired
    EntityDao entityDao;
    @Autowired
    Environment env;
    private static ObjectMapper objectMapper = new ObjectMapper();
    public String postArticle(FeedArticle article){
        String ret = null;
        String body = buildArticlePostBody(article);
        Map<String,Object> headers = new HashMap<>();
        headers.put("Authorization",env.getProperty("article.post.authorization"));
        headers.put("Content-Type","application/hal+json");
        try {
            //logger.info("article body: {}", body);
            String response = HttpClientUtil.httpPostRequest(DrupalConstant.articlePostUrl,body,headers,false,null);
            //logger.info("article post response: {}",response);
            JSONObject root = new JSONObject(response);
            ret = root.getJSONArray("nid").getJSONObject(0).getString("value");
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.toString());
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
        headers.put("Authorization",env.getProperty("article.post.authorization"));
        headers.put("Content-Type","application/hal+json");
        String body = DrupalConstant.tagPostContent.replaceAll("###tag###",tag.trim());
        try {
            String response = HttpClientUtil.httpPostRequest(DrupalConstant.tagPostUrl,body,headers,false,null);
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

        JSONArray titleArr = new JSONArray();
        JSONObject title = new JSONObject();
        title.put("value",article.getTitle());
        title.put("lang","zh-hans");
        titleArr.put(title);
        root.put("title",titleArr);

        JSONArray bodyArr = new JSONArray();
        JSONObject body = new JSONObject();
        body.put("value",article.getContent());
        body.put("summary",article.getDescription()==null?"":article.getDescription());
        body.put("lang","zh-hans");
        body.put("format","full_html");
        bodyArr.put(body);
        root.put("body",bodyArr);

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
                                "          \"type\": {\n" +
                                "            \"href\": \"http://www.bigdataway.net/rest/type/taxonomy_term/tags\"\n" +
                                "          }\n" +
                                "        }");
                        tagNode.put("_links",type);
                        tags.put(tagNode);

                    }
                }
            }
        }

        String category = article.getCategory();
        if(category!=null && DrupalConstant.categoryUuidMap.get(category)!=null){
            String uuid = DrupalConstant.categoryUuidMap.get(category);
            JSONArray categories = new JSONArray();
            JSONObject cNode = new JSONObject();
            cNode.put("lang", "zh-hans");
            JSONArray uuidArr = new JSONArray("[\n" +
                    "          {\n" +
                    "            \"value\": \"" + uuid + "\"\n" +
                    "          }\n" +
                    "        ]");
            cNode.put("uuid",uuidArr);
            JSONObject type = new JSONObject("{\n" +
                    "          \"type\": {\n" +
                    "            \"href\": \"http://www.bigdataway.net/rest/type/taxonomy_term/category\"\n" +
                    "          }\n" +
                    "        }");
            cNode.put("_links",type);
            categories.put(cNode);
            _embedded.put("http://www.bigdataway.net/rest/relation/node/article/field_category",categories);
        }

        _embedded.put("http://www.bigdataway.net/rest/relation/node/article/field_tags",tags);
        root.put("_embedded",_embedded);

        String originUrl = article.getOriginalUrl();
        if(StringUtils.trimToNull(originUrl)==null)
            originUrl = article.getUrl();
        if(originUrl!=null && originUrl.startsWith("http")){
            JSONArray urlArr = new JSONArray();
            JSONObject originUrlNode = new JSONObject();
            originUrlNode.put("uri",originUrl);
            originUrlNode.put("title","点击阅读原文");
            urlArr.put(originUrlNode);
            root.put("field_originurl",urlArr);
        }

        return root.toString();
    }
}
