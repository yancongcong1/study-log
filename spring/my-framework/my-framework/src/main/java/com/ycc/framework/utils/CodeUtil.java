package com.ycc.framework.utils;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * created by ycc at 2018\4\24 0024
 */
public class CodeUtil {
    private static final Logger logger = Logger.getLogger(CodeUtil.class);

    public static String encodeUrl(String source) {
        String target;
        try {
            target = URLEncoder.encode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("转码错误", e);
            throw new RuntimeException( e);
        }
        return target;
    }

    public static String decodeUrl(String source) {
        String target;
        try {
            target = URLDecoder.decode(source, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            logger.error("解码错误", e);
            throw new RuntimeException( e);
        }
        return target;
    }
}
