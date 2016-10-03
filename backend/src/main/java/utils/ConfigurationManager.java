package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

public class ConfigurationManager {

    private Map<String, String> env = System.getenv();
    private String config_filename;
    private Properties prop = new Properties();

    public ConfigurationManager() {
        // read config file location from sys
        config_filename = env.get("JOIN_CONFIG");
        if (config_filename == null) {
            throw new RuntimeException("Couldn't access config file, did you set JOIN_CONFIG in enviornment?");
        }

        // load the properties
        InputStream input = null;
        try {
            input = new FileInputStream(config_filename);
            prop.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (input != null) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    // get setting from the config
    public String get(String system_name) {
        if (prop.getProperty(system_name) == null) {
            throw new RuntimeException("Property " + system_name + " not found in config file " + config_filename + ".");
        }
        return prop.getProperty(system_name);
    }

}
