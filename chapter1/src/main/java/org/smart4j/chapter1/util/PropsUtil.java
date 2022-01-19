package org.smart4j.chapter1.util;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
//属性文件工具类
public class PropsUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(PropsUtil.class);//不太懂
    public static Properties loadProps(String fileName) throws FileNotFoundException {
        Properties props = null;
        InputStream inputStream = null;
        try {
            inputStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
            if(inputStream==null){
                throw new FileNotFoundException(fileName+"file is not fount");
            }
            props = new Properties();
            props.load(inputStream);
        } catch (IOException e) {
            LOGGER.error("load properties file failure",e);
        }finally {
            if (inputStream!=null){
                try {
                    inputStream.close();
                } catch (IOException e) {
                    LOGGER.error("close input steam failure",e);
                }
            }
        }
        return props;
    }

    //获取字符型属性
    public static String getString(Properties props,String key,String defaultValue){
        String value = defaultValue;
        if(props.containsKey(key)){
            value=props.getProperty(key);
        }
        return value;
    }
    //获取数值型属性
    public static int getInt(Properties properties,String key){

        return getInt(properties,key,0);
    }

    //获取数值型属性
    public static int getInt(Properties properties,String key ,int defaultValue){
        int value = defaultValue;
        if (properties.containsKey(key)){
            return getInt(properties,key,0);
        }
        return value;
    }

    //获取布尔型属性
    public static boolean getBoolean(Properties properties,String key){
       return getBoolean(properties,key,false);

    }

    //获取布尔类型，可提供默认值
    public static boolean getBoolean(Properties properties,String key,Boolean defaultValue){
        boolean value = defaultValue;
        if (properties.containsKey(key)) {
            value = CastUtil.castBoolean(properties.getProperty(key));
        }
        return getBoolean(properties,key,defaultValue);
    }


}
