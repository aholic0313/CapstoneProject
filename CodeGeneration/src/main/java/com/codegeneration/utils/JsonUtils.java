package com.codegeneration.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtils {
    public static String convertObj2Json(Object o){
        if (null == o){
            return null;
        }
        return JSON.toJSONString(o, SerializerFeature.DisableCircularReferenceDetect);
    }
}
