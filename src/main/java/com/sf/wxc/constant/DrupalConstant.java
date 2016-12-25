package com.sf.wxc.constant;

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
}
