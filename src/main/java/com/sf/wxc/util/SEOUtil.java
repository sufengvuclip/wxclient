package com.sf.wxc.util;

import java.io.UnsupportedEncodingException;
import java.util.List;

/**
 * Created by Su Feng on 2017/1/6.
 */
public class SEOUtil {
    static final String baiduPostUrl = "http://data.zz.baidu.com/urls?site=www.bigdataway.net&token=0aqOWpP8u9cdmP3j";
    public static boolean postToBaidu(List<String> urls){
        boolean ret = false;
        StringBuffer sb = new StringBuffer();
        if(urls!=null && urls.size()>0){
            for(String url:urls)
                sb.append(url).append("\n");
            try {
                String response = HttpClientUtil.httpPostRequest(baiduPostUrl,sb.substring(0,sb.length()-1));
                System.out.println(response);
                if(response.contains("success"))
                    ret = true;
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

/*    public static void main(String[] args) {
        List<String> urls = new ArrayList<>();
        urls.add("http://www.bigdataway.net/node/1185");
        urls.add("http://www.bigdataway.net/node/1186");
        boolean ret = postToBaidu(urls);
    }*/
}
