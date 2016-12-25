package org.github.wks.jhql.util;

/**
 * Created by Administrator on 2016-12-26.
 */
public class CopyRightUtil {
    static String[] copyRightArr = new String[]{
        "不代表今日头条立场",
        "不得转载"
    };
    public static boolean containCopyRightInfo(String content){
        for(String cr: copyRightArr){
            if(content.contains(cr)){
                return true;
            }
        }
        return false;
    }
}
