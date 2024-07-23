package com.codegeneration.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

// 配置文件读取工具
public class PropertiesUtils {
    private static Properties properties = new Properties();
    private static Map<String,String> PROPER_MAP = new ConcurrentHashMap<>();

    static{
        InputStream is = null;
        try {
            is = PropertiesUtils.class.getClassLoader().getResourceAsStream("application.properties");
            properties.load(is);
            Iterator<Object> iterator = properties.keySet().iterator();
            while (iterator.hasNext()){
                String key = (String) iterator.next();
                PROPER_MAP.put(key,properties.getProperty(key));
            }
        }catch (Exception e){
            if (is!=null){
                try {
                    is.close();
                } catch (IOException ioException) {
                    ioException.printStackTrace();
                }
            }
        }
    }

    // 读取配置文件
    public static String getString(String key){
        return PROPER_MAP.get(key);
    }

    public static void main(String[] args) {
        System.out.println(getString("db.driver.name"));
    }
}
