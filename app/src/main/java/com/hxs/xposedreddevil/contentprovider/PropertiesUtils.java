package com.hxs.xposedreddevil.contentprovider;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Properties;

public class PropertiesUtils {

    public PropertiesUtils() {
    }

    public static String getValue(String fileName, String key, String defaultValue) {
        return getProperties(fileName).getProperty(key, defaultValue);
    }

    public static void putValue(String fileName, String key, String value) {
        try {
            Properties properties = getProperties(fileName);
            OutputStream fos = new FileOutputStream(fileName);
            properties.setProperty(key, value);
            properties.store(fos, "Update value");
        } catch (IOException var5) {
            var5.printStackTrace();
        }

    }

    private static Properties getProperties(String fileName) {
        Properties properties = new Properties();

        try {
            File file = new File(fileName);
            if (!file.exists()) {
                File path = new File(file.getParent());
                if (!path.exists()) {
                    path.mkdirs();
                }

                file.createNewFile();
            }

            InputStream inputStream = new FileInputStream(file);
            properties.load(inputStream);
        } catch (IOException var4) {
            var4.printStackTrace();
        }

        return properties;
    }
}
