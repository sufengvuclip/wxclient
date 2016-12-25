package com.sf.wxc.constant;

import java.util.HashMap;

/**
 * Created by Administrator on 2016-12-25.
 */
public class DrupalConstant {
    public static String tagPostUrl = "http://www.bigdataway.net/entity/taxonomy_term/?_format=hal_json";
    public static String tagPostContent = "{\n" +
            "  \"_links\": {\n" +
            "    \"type\": {\n" +
            "      \"href\": \"http://www.bigdataway.net/rest/type/taxonomy_term/tags\"\n" +
            "    }\n" +
            "  },\n" +
            "  \"vid\": [\n" +
            "    {\n" +
            "      \"target_id\": \"tags\"\n" +
            "    }\n" +
            "  ],\n" +
            "  \"name\": [\n" +
            "    {\n" +
            "      \"value\": \"###tag###\",\n" +
            "      \"lang\": \"zh-hans\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

    public static String articlePostUrl = "http://www.bigdataway.net/entity/node";

    public static HashMap<String, String> categoryUuidMap = new HashMap<>();
    static{
        categoryUuidMap.put("tech","7751d246-4d74-4d59-8cfd-1cbb97ec3b73");
        categoryUuidMap.put("news","a36327f8-a056-43d4-96b2-1080bdd4b900");
        categoryUuidMap.put("crawler","8fbd9a28-b4f8-4c8c-95db-700f70aee421");
        categoryUuidMap.put("news_crawler","dcbc0608-6941-45ba-8a31-dd568a4c777e");
        categoryUuidMap.put("search","38607da2-8339-4b86-a1a0-2578da0e3597");
        categoryUuidMap.put("news_search","60f3e3b1-0b5f-4d68-a621-6b9faa02f3f7");
        categoryUuidMap.put("nlp","6975b4bd-3604-42a4-90f3-0c24110eb3d4");
        categoryUuidMap.put("news_nlp","a4aca62d-7f9b-4db0-85cc-1310d3bedae9");
        categoryUuidMap.put("machinelearning","62abae4f-b73e-4aeb-8e92-5d27382b7be2");
        categoryUuidMap.put("news_machinelearning","423f1f06-24ee-40bd-ad86-cb2e66ded42f");
        categoryUuidMap.put("bigdata","4f4ba301-417f-4f96-8286-85efa992cda7");
        categoryUuidMap.put("news_bigdata","3ff4e202-9848-4470-b251-546be1472529");
    }

    /**
     *
     8fbd9a28-b4f8-4c8c-95db-700f70aee421  爬虫技术
     dcbc0608-6941-45ba-8a31-dd568a4c777e  爬虫资讯
     38607da2-8339-4b86-a1a0-2578da0e3597  搜索技术
     60f3e3b1-0b5f-4d68-a621-6b9faa02f3f7  搜索资讯
     6975b4bd-3604-42a4-90f3-0c24110eb3d4  自然语言处理
     a4aca62d-7f9b-4db0-85cc-1310d3bedae9  自然语言处理资讯
     62abae4f-b73e-4aeb-8e92-5d27382b7be2  机器学习
     423f1f06-24ee-40bd-ad86-cb2e66ded42f  机器学习资讯
     4f4ba301-417f-4f96-8286-85efa992cda7  大数据技术
     3ff4e202-9848-4470-b251-546be1472529  大数据资讯
     7751d246-4d74-4d59-8cfd-1cbb97ec3b73  技术
     a36327f8-a056-43d4-96b2-1080bdd4b900  资讯
     */
}
