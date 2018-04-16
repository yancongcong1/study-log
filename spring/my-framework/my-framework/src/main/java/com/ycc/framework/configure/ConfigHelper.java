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
        return properties.getProperty(Constants.DATABASE_DRIVER);
    }

    public static String getDatabaseUrl() {
        return properties.getProperty(Constants.DATABASE_URL);
    }

    public static String getDatabaseUsername() {
        return properties.getProperty(Constants.DATABASE_USERNAME);
    }

    public static String getDatabasePassword() {
        return properties.getProperty(Constants.DATABASE_PASSWORD);
    }

    public static String getBasePackage() {
        return properties.getProperty(Constants.BASE_PACKAGE);
    }

    public static String getJspPath() {
        return properties.getProperty(Constants.JSP_PATH);
    }

    public static String getAssetPath() {
        return properties.getProperty(Constants.ASSET_PATH);
    }
}
