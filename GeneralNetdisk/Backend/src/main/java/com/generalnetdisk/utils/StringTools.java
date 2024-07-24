package com.generalnetdisk.utils;

import org.apache.commons.lang3.RandomStringUtils;

public class StringTools {
    /**
     * 生成随机数
     *
     * @param count
     * @return
     */
    public static final String getRandomNumber(Integer count) {
        return RandomStringUtils.random(count, false, true);
    }
}