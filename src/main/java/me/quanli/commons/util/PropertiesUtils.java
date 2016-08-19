/*
 * Copyright (c) 2010-2011 IFLYTEK. All Rights Reserved.
 * [Id:ErcConfigConstant.java  2012-07-05 下午4:03 poplar.yfyang ]
 */
package me.quanli.commons.util;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

/**
 * 系统配置文件读取工具
 * 
 * @author Administrator
 */
public class PropertiesUtils {

    /** 资源文件中配置的信息 */
    private static Map<String, String> properties = new HashMap<String, String>();

    /**
     * 设置资源文件中的配置信息
     * 
     * @param properties
     *            配置信息
     */
    public static void setProperties(Map<String, String> properties) {
        PropertiesUtils.properties.putAll(properties);
    }

    public static Map<String, String> getProperties() {
        return properties;
    }

    /**
     * 根据KEY取得资源配置信息
     * 
     * @param key
     *            key，资源文件中配置的key
     * @return 资源配置信息
     */
    public static String getProperty(String key) {

        String result = properties.get(key);
        if (StringUtils.isEmpty(result)) {
            result = "";
        }
        return result.trim();
    }

    public static String getProperty(String key, String defVal) {
        String result = properties.get(key);
        if (StringUtils.isEmpty(result)) {
            return defVal;
        }
        return result.trim();
    }

    /**
     * 根据KEY删除资源文件中的配置信息
     * 
     * @param key
     *            key，资源文件中配置的key
     */
    public static void removeProperty(String key) {
        properties.remove(key);
    }

}
