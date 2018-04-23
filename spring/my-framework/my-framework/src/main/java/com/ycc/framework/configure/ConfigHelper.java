package com.ycc.framework.configure;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;

/**
 * Get value from property files which define in the {@link Constants}.
 *
 * created by ycc at 2018\4\16 0016
 */
public class ConfigHelper {
    private static Logger logger = Logger.getLogger(ConfigHelper.class);

    private static InputStream inputStream;
    private static Properties properties = new Properties();

    static {
        try {
            inputStream = ClassLoader.getSystemResourceAsStream(Constants.CONFIG_FILE);
            properties.load(inputStream);
        } catch (IOException e) {
            logger.error("Reader error");
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                logger.error("Close file error");
            }
        }
    }

    public static String getDatabaseDriver() {
        return getString(Constants.DATABASE_DRIVER);
    }

    public static String getDatabaseUrl() {
        return getString(Constants.DATABASE_URL);
    }

    public static String getDatabaseUsername() {
        return getString(Constants.DATABASE_USERNAME);
    }

    public static String getDatabasePassword() {
        return getString(Constants.DATABASE_PASSWORD);
    }

    public static String getBasePackage() {
        return getString(Constants.BASE_PACKAGE);
    }

    public static String getJspPath() {
        return getString(Constants.JSP_PATH);
    }

    public static String getAssetPath() {
        return getString(Constants.ASSET_PATH);
    }

    private static String getString (String key) {
        return properties.getProperty(key);
    }

    private static String getString(String key, String defaultVal) {
        return properties.getProperty(key, defaultVal);
    }
}
