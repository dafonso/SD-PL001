package common.properties;

import java.util.Properties;

public class CommonProps {

    private static final String SERVER_PORT = "serverPort";
    private static final String SERVER_HOST = "serverHost";
    private static Properties commonProperties;

    public static int getServerPort() {
        if (commonProperties == null) {
            commonProperties = PropertiesManager.loadCommonProps();
        }
        return Integer.parseInt(commonProperties.getProperty(SERVER_PORT));
    }
    public static String getServerHost() {
        if (commonProperties == null) {
            commonProperties = PropertiesManager.loadCommonProps();
        }
        return commonProperties.getProperty(SERVER_HOST);
    }
}
