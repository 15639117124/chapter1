package org.smart4j.chapter1.util;

import org.apache.commons.lang3.StringUtils;

import java.util.zip.DataFormatException;

public class CastUtil {

    //转化为String
    public static String castString(Object o){
        return CastUtil.castString(o,"");//如果o为null，那么就返回一个默认值“”空字符串
    }
    //转化为String并提供默认值
    public static String castString(Object o,String defaultValue){
        return o != null ? String.valueOf(o) : defaultValue;//三元表达式
    }


    //转化为double
    public static double castDouble(Object obj){
        return CastUtil.castDouble(obj,0);
    }
    //转为double,并提供默认值
    public static double castDouble(Object obj,double defaultValue){
        double doubleValue = defaultValue;
        if (obj!=null){
            String stringValue = castString(obj);
            if (StringUtil.isNotEmpty(stringValue)){
                try {
                    doubleValue = Double.parseDouble(stringValue);
                }catch (NumberFormatException e){
                    doubleValue = defaultValue;
                }
            }
        }
        return doubleValue;
    }

    //转化为long类型
    public static long castLong(Object obj){
        return CastUtil.castLong(obj,0);
    }

    //转化为long类型，并提供默认值
    public static long castLong(Object obj,long defaultValue){
        long longValue = defaultValue;
        if (obj!=null){
            String stringValue = castString(obj);
            if (StringUtil.isNotEmpty(stringValue)){

                try {
                    longValue = Long.parseLong(stringValue);
                }catch (NumberFormatException e){
                    longValue = defaultValue;
                }
            }
        }
        return longValue;
    }

    //转化为int类型
    public static int castInt(Object obj){
        return castInt(obj,0);
    }

    //转化为Int类型，并提供默认值
    public static int castInt(Object obj,int defaultValue){
        int intValue = defaultValue;
        if (obj!=null){
            String stringValue = castString(obj);
            if (StringUtil.isNotEmpty(stringValue)){
                try {
                    intValue = Integer.valueOf(stringValue);
                }catch (NumberFormatException e){
                    intValue = defaultValue;
                }
            }
        }
        return intValue;
    }

    //转化为boolean
    public static boolean castBoolean(Object obj){
        return castBoolean(obj,false);
    }
    //转化为boolean并提供默认值
    public static boolean castBoolean(Object obj,boolean defaultValue){
        boolean booleanValue = defaultValue;
        if (obj!=null){
            booleanValue = Boolean.parseBoolean(castString(obj));
        }
        return booleanValue;
    }
}
