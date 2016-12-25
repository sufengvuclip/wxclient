package com.sf.wxc.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sf.wxc.beans.Article;
import com.sf.wxc.util.HttpClientUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Component;

import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2016-12-18.
 */
@Component
public class TT {
    private static String search_req = "http://m.toutiao.com/search/?keyword={0}&from=search_tab";
    private static String domain = "http://m.toutiao.com/";
    public List<Article> search(String keyword){
        List<Article> ret = new ArrayList<>();
        try {
            String search_url = MessageFormat.format(search_req, URLEncoder.encode(keyword,"UTF-8"));
            String response = HttpClientUtil.httpGetRequest(search_url);
            Document doc = Jsoup.parse(response,"UTF-8");
            Elements links =doc.select("a[class='article_link clearfix']");
            ObjectMapper mapper = new ObjectMapper();
            List<SearchResult> searchResult = new ArrayList<>();
            for(Element element : links){
                try {
                    String articleLink = null;
                    String firstImage = null;
                    try {
                        articleLink = domain + element.attr("href");
                        firstImage = element.select("div[class='list_img_holder'").get(0).select("img").attr("src");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if(firstImage!=null && articleLink!=null){
                        searchResult.add(new SearchResult(articleLink,firstImage));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
            }

            if(searchResult!=null && searchResult.size()>0){
                for(SearchResult result: searchResult){
                    String finalUrl = HttpClientUtil.httpGetRedirectFinalUrl(result.getLink());
                    String thumbnailUrl = result.getThumbnailUrl();
                    System.out.println("finalUrl="+finalUrl);
                    if(finalUrl!=null && finalUrl.startsWith("http://m.toutiao.com/i")) {
                        String articleJson = HttpClientUtil.httpGetRequest(finalUrl+"info");
                        Map jsonmap = mapper.readValue(articleJson, Map.class);
                        String title = (String) ((Map)jsonmap.get("data")).get("title");
                        String content = (String) ((Map)jsonmap.get("data")).get("content");
                        System.out.println("=============="+articleJson);
                        System.out.println("===="+title);
                        Article article = new Article();
                        article.setTitle(title);
                        article.setContent(content);
                        article.setShow_cover_pic("1");
                        article.setThumb_media_id("qI6_Ze_6PtV7svjolgs-rN6stStuHIjs9_DidOHaj0Q-mwvBelOXCFZiq2OsIU-p");
                        article.setOriginal_thumbnail_url(thumbnailUrl);
                        ret.add(article);
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ret;
    }

    @Data
    @AllArgsConstructor
    private class SearchResult{
        private String link;
        private String thumbnailUrl;
    }

/*    public static void main(String[] args) {
        TT tt = new TT();
        tt.search("搞笑");
    }*/
}
