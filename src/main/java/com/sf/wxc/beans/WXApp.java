package com.sf.wxc.beans;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.sf.wxc.util.HttpClientUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.text.MessageFormat;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2016-12-17.
 */
@Data
@AllArgsConstructor
public class WXApp {
    private static Cache<String, String> tokenCache = CacheBuilder.newBuilder().expireAfterWrite(100, TimeUnit.MINUTES).build();
    public static String token_req = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid={0}&secret={1}";

    private String appID;
    private String appsecret;
    private String name;
    private String account;

    public String getToken(){
        String token =  tokenCache.getIfPresent(this.appID);
        if(StringUtils.trimToNull(token)==null){
            token = getToken(this);
            if(token!=null){
                tokenCache.put(this.appID,token);
            }
        }
        return token;
    }

    private String getToken(WXApp wxApp){
        String ret = null;
        String token_url = MessageFormat.format(token_req,wxApp.getAppID(),wxApp.getAppsecret());
        String response = HttpClientUtil.httpGetRequest(token_url);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            Map map = objectMapper.readValue(response, Map.class);
            ret = (String) map.get("access_token");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }
}
