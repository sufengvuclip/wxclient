package com.sf.wxc.util;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;

/**
 * Created by Administrator on 2016-12-24.
 */
public class SeleniumUtil {
    static HtmlUnitDriver driver = null;
    static{
        //System.setProperty("phantomjs.binary.path","D:\\TDDOWNLOAD\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        //driver = new org.openqa.selenium.phantomjs.PhantomJSDriver();
        driver = new HtmlUnitDriver(true);
        //driver.setJavascriptEnabled(true);

    }
    public static String getRequest(String link){
        String ret = null;
        driver.get(link);
        ret = driver.getPageSource();
//        driver.close();
        return ret;
    }

    public static void main(String[] args) {
        String content = getRequest("https://www.toutiao.com/a6431773483667898626/");
        System.out.println(content);
    }
}
