package com.generalnetdisk.utils;

public class StringUtils {
    public static String UpperCaseFirstLetter(String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return s;
        }
        return s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    public static String LowerCaseFirstLetter(String s) {
        if (org.apache.commons.lang3.StringUtils.isEmpty(s)) {
            return s;
        }
        return s.substring(0, 1).toLowerCase() + s.substring(1);
    }

}
