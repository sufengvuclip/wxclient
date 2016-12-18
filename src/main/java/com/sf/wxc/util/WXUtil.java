package com.sf.wxc.util;

import com.sf.wxc.beans.Article;
import com.sf.wxc.beans.WXApp;
import com.sf.wxc.constant.MaterialType;
import com.sf.wxc.service.WX;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.File;

/**
 * Created by Administrator on 2016-12-17.
 */
@Component
public class WXUtil {
    @Resource
    WX wx;
    @Resource
    private Environment env;

    //private String imagePath = env.getProperty("img.download.root");
    public Article convertArticle2wx(Article originalArticle, WXApp wxApp){
        String content = originalArticle.getContent();
        String newContent = convertContent2wx(content,"",wxApp);

        String localImg = downloadImg(originalArticle.getOriginal_thumbnail_url());
        String thumbnail_id = wx.uploadmaterial(localImg,wxApp, MaterialType.THUMB);

        if(newContent!=null && thumbnail_id!=null) {
            originalArticle.setContent(newContent);
            originalArticle.setThumb_media_id(thumbnail_id);
        }else
            return null;
        return originalArticle;
    }


    /**
     * remove script and img transfer
     * @param content
     * @return
     */
    public String convertContent2wx(String content,String imgDomain, WXApp wxApp){
        String ret = null;
        if(content==null) return null;
        Document doc = Jsoup.parse(content,"UTF-8");
        Elements imgs = doc.select("img[src]");
        for (Element element : imgs) {
            String imgUrl = element.attr("src");
            if (imgUrl.trim().startsWith("/")) {
                imgUrl =imgDomain + imgUrl;
            }
            try {
                String localImg = downloadImg(imgUrl);
                File localfile = new File(localImg);
                if(localfile.exists() && FileUtils.sizeOf(localfile)<2*1024*1024) {// size less than 2m
                    String wxImgUrl = wx.uploadimg(localImg, wxApp);
                    element.attr("src", wxImgUrl);
                }else{
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }
        return doc.select("body").html();
    }

    public String downloadImg(String url){
        String ext = FilenameUtils.getExtension(url);
        if(StringUtils.stripToNull(ext)==null){
            ext = "jpg";
        }
        String filepath = env.getProperty("img.download.root")+ File.separator+System.currentTimeMillis()+"."+ext;
        String ret = HttpClientUtil.download(url,filepath);
        return ret;
    }
}
