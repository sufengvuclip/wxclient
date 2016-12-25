package com.sf.wxc.util;

import org.openqa.selenium.WebDriver;

/**
 * Created by Administrator on 2016-12-24.
 */
public class SeleniumUtil {
    static WebDriver driver = null;
    static{
        System.setProperty("phantomjs.binary.path","D:\\TDDOWNLOAD\\phantomjs-2.1.1-windows\\bin\\phantomjs.exe");
        driver = new org.openqa.selenium.phantomjs.PhantomJSDriver();
    }
    public static String getRequest(String link){
        String ret = null;
        driver.get(link);
        ret = driver.getPageSource();
//        driver.close();
        return ret;
    }
}
