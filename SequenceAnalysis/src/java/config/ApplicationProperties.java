/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 *
 * @author dongmei
 */
public class ApplicationProperties {

    private static ApplicationProperties instance;
    private static Properties properties;

    public static ApplicationProperties getInstance() {
        if (instance == null) {
            instance = new ApplicationProperties();
        }
        return instance;
    }

    private ApplicationProperties() {
        loadProperties();
    }

    private static void loadProperties() {
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            InputStream in = classLoader.getResourceAsStream("/config/SystemConfig.properties");
            properties = new Properties();
            properties.load(in);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * @return the properties
     */
    public Properties getProperties() {
        return properties;
    }
}
