package com.ycc.framework.utils;

import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Read the input stream and transport it to a string.
 *
 * created by ycc at 2018\4\24 0024
 */
public class StreamUtil {

    private static final Logger logger = Logger.getLogger(StreamUtil.class);

    public static String getStream(InputStream is) {
        StringBuilder stringBuilder = new StringBuilder();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
        } catch (IOException e) {
            logger.error("读取输入流错误", e);
            throw new RuntimeException(e);
        }
        return stringBuilder.toString();
    }
}
