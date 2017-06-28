package com.darkidiot.http.util;

/**
 * Copyright (c) 2016 成都国美大数据
 * Date:2017/6/28
 * Author: 2016年 <a href="heqiao2@gome.com.cn">darkidiot</a>
 * Desc:
 */
public class StringUtils {

    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
}
