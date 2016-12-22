package com.sf.wxc.util;

import org.apache.commons.lang3.math.NumberUtils;

import java.math.BigInteger;
import java.sql.Date;

/**
 * Created by Administrator on 2016-08-19.
 */
public class ConvertUtils {
    public static Object convert(Object value, Class<?> clazz){
        //System.out.println("convert: value "+value+" class "+clazz.getName());
        if(clazz.equals(String.class))
            return String.valueOf(value);
        if(clazz.equals(Integer.class) || "int".equals(clazz.getName()))
            return NumberUtils.toInt(String.valueOf(value),0);
        if(clazz.equals(Long.class) || "long".equals(clazz.getName()))
            return NumberUtils.toLong(String.valueOf(value),0);
        if(clazz.equals(Double.class) || "double".equals(clazz.getName()))
            return NumberUtils.toDouble(String.valueOf(value),0);
        if(clazz.equals(Float.class) || "float".equals(clazz.getName()))
            return NumberUtils.toFloat(String.valueOf(value),0);
        if(clazz.equals(BigInteger.class))
            return NumberUtils.toInt(String.valueOf(value),0);
        if(clazz.equals(Date.class)) return new java.util.Date(((Date) value).getTime());
        return value;
    }
}
