package com.sf.wxc.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sf.wxc.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;

/**
 * Created by Administrator on 2016-12-17.
 */
@Controller
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/tor")
    public ResponseEntity<?> tor(@RequestParam(value = "url", required = true) String url) throws IOException {
        if (StringUtils.trimToNull(url) == null)
            return new ResponseEntity<String>("invalidate parameter!", HttpStatus.FORBIDDEN);
        ObjectMapper objectMapper = new ObjectMapper();
        JSONObject login = new JSONObject(loginJson);

        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        String result = null;
        try {
            result =HttpClientUtil.httpGetRequest(url, false,login,true);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity<String>(result, HttpStatus.OK);

    }

    static String loginJson = "{\n" +
            "  \"loginurl\": \"http://www.tuicool.com/login\",\n" +
            "  \"formdata\":{\n" +
            "    \"utf8\":\"✓\",\n" +
            "    \"authenticity_token\":\"5vg/Z5gF6fAL2cgHONRAHjIqI2PG8zxjB5H5AzpwLOk=\",\n" +
            "    \"email\":\"jiqixuexinlp@gmail.com\",\n" +
            "    \"password\":\"meiyoumima\",\n" +
            "    \"remember\":\"1\"\n" +
            "  },\n" +
            "  \"headers\":{\n" +
            "    \"Accept\":\"text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8\",\n" +
            "    \"Accept-Encoding\":\"gzip, deflate\",\n" +
            "    \"Accept-Language\":\"zh-CN,zh;q=0.8,en;q=0.6\",\n" +
            "    \"Cache-Control\":\"max-age=0\",\n" +
            "    \"Connection\":\"keep-alive\",\n" +
            "    \"Content-Type\":\"application/x-www-form-urlencoded\",\n" +
            "    \"Host\":\"www.tuicool.com\",\n" +
            "    \"Origin\":\"http://www.tuicool.com\",\n" +
            "    \"Referer\":\"http://www.tuicool.com/login\",\n" +
            "    \"Upgrade-Insecure-Requests\":\"1\",\n" +
            "    \"User-Agent\":\"Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.71 Safari/537.36\"\n" +
            "  }\n" +
            "}";
}
