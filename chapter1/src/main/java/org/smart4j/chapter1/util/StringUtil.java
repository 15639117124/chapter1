package org.smart4j.chapter1.util;

import org.apache.commons.lang3.StringUtils;

public class StringUtil {


    //判断字符串是否为空
    //感觉这方法毫无意义，人家直接用StringUtils不久得了，为啥还要调你
    public static boolean isEmpty(String str){
        if (str!=null){
            str = str.trim();
        }
        return StringUtils.isEmpty(str);
    }

    //判断字符串是否非空
    public static boolean isNotEmpty(String str){
        return isEmpty(str);
    }
}
