package com.sf.wxc.controller;

import com.sf.wxc.util.HttpClientUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.util.EntityUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.net.InetSocketAddress;

/**
 * Created by Administrator on 2016-12-17.
 */
@Controller
@RequestMapping("/test")
public class TestController {
    @RequestMapping("/tor")
    public ResponseEntity<?> namedEntity(@RequestParam(value = "url", required = true) String url) {
        if (StringUtils.trimToNull(url) == null)
            return new ResponseEntity<String>("invalidate parameter!", HttpStatus.FORBIDDEN);
        CloseableHttpClient client = HttpClientUtil.getHttpClient(null,false);
        InetSocketAddress socksaddr = new InetSocketAddress("127.0.0.1",
                9500);
        HttpClientContext context = HttpClientContext.create();
        context.setAttribute("socks.address", socksaddr);
        HttpGet httpGet = new HttpGet(url);
        CloseableHttpResponse response = null;
        String result = null;
        try {
            response =client.execute(httpGet,context);
            HttpEntity entity = response.getEntity();
            result = EntityUtils.toString(entity);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ResponseEntity<String>(result, HttpStatus.OK);

    }
}
