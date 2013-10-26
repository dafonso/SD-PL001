package common.properties;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesManager {

    private static final String COMMON_PROPS_FILENAME = "common_config.properties";

    public static Properties loadCommonProps() {
        return loadProps(COMMON_PROPS_FILENAME);
    }

    @SuppressWarnings("resource")
    private static Properties loadProps(String filename) {
        Properties props = new Properties();
        InputStream is = null;

        // First try loading from the current directory
        try {
            File f = new File(filename);
            is = new FileInputStream(f);
        } catch (Exception e) {
            is = null;
        }

        try {
            if (is == null) {
                // Try loading from classpath
                is = PropertiesManager.class.getResourceAsStream(filename);
            }

            // Try loading properties from the file (if found)
            props.load(is);
            is.close();
        } catch (Exception e) {
        }

        return props;
    }
}
