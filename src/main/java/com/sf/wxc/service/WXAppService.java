package com.sf.wxc.service;

import com.sf.wxc.beans.WXApp;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * Created by Administrator on 2016-12-18.
 */
@Component
public class WXAppService {
    private static HashMap<String,WXApp> appMap = new HashMap<>();
    public static final String TEST_SF = "wxc8e7ddcc644a86b3";
    public static final String TEST_XK = "wx7cd5063a22355f21";
    public static final String QIQUSOULUO = "wx72329325cec0e6d4";
    static{
        appMap.put(TEST_SF, new WXApp("wxc8e7ddcc644a86b3","da437c25c4ddda9bf3fdb5969dbfbcd9","测试账号1","sufengster@163.com"));
        appMap.put(TEST_XK, new WXApp("wx7cd5063a22355f21","9c1102d84f4d6d1c20ccff403aac64cc","测试账号2","sufengster@163.com"));
        appMap.put(QIQUSOULUO, new WXApp("wx72329325cec0e6d4","f574edf53ce6c1a24566fdc9133f85ae","世界奇趣大搜罗","qiqusouluo@163.com"));
    }

    public WXApp getWXApp(String appid){
        return appMap.get(appid);
    }
}
