package common.properties;

import java.util.Properties;

public class CommonProps {

    private static final String SERVER_PORT = "serverPort";
    private static final String SERVER_HOST = "serverHost";
    private static final String SERVER_POOL = "serverPool";
    
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

    public static String[][] getServerPool() {
        if (commonProperties == null) {
            commonProperties = PropertiesManager.loadCommonProps();
        }
        //get array split up by the semicolin
        String[] a = commonProperties.getProperty(SERVER_POOL).split(";");

        //create the two dimensional array with correct size
        String[][] array = new String[a.length][4];

        //combine the arrays split by semicolin and comma 
        for (int i = 0; i < a.length; i++) {
            array[i] = a[i].split(",");
        }
        return array;
    }
}
