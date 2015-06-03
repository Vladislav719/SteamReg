package com.github.vladislav719.help;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by vladislav on 02.06.2015.
 */
public final class PropHelper {

    public String getPropByKey(String key) {
        Properties prop = new Properties();
        InputStream input = null;
        String apiKey = null;

        input = getClass().getClassLoader().getResourceAsStream("config.properties");
        try {
            prop.load(input);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return prop.getProperty(key);
    }
}
