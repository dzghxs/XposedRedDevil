package com.hxs.xposedreddevil.contentprovider;

import android.content.Context;
import android.os.Environment;

import com.hxs.xposedreddevil.utils.DateUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Writer;
import java.util.Properties;

import static com.hxs.xposedreddevil.utils.Constant.RED_LIST;

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
            properties.setProperty(key, new String(value.getBytes(), "utf-8"));
            properties.store(fos, "Update value");
        } catch (Exception var5) {
            var5.printStackTrace();
            System.out.println("存储错误:" + var5);
        }

    }

    public static void putListValue(String value) {
        try {
            Properties properties = getProperties(RED_LIST );
            Writer writer = new FileWriter(RED_LIST);
            writer.write(value);
            properties.store(writer, "Update value");
            writer.close();
        } catch (Exception var5) {
            var5.printStackTrace();
            System.out.println("存储错误:" + var5);
        }

    }

    /**
     * 从sd card文件中读取数据
     *
     * @return
     * @throws IOException
     */
    public static String readExternal() throws IOException {
        StringBuilder sb = new StringBuilder("");
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //打开文件输入流
            FileInputStream inputStream = new FileInputStream(RED_LIST);

            byte[] buffer = new byte[1024];
            int len = inputStream.read(buffer);
            //读取文件内容
            while (len > 0) {
                sb.append(new String(buffer, 0, len));

                //继续将数据放到buffer中
                len = inputStream.read(buffer);
            }
            //关闭输入流
            inputStream.close();
        }
        return sb.toString();
    }

    /**
     * 将内容写入sd卡中
     *
     * @param content 待写入的内容
     * @throws IOException
     */
    public static void writeExternal(String content) throws IOException {

        //获取外部存储卡的可用状态
        String storageState = Environment.getExternalStorageState();

        //判断是否存在可用的的SD Card
        if (storageState.equals(Environment.MEDIA_MOUNTED)) {

            //路径： /storage/emulated/0/Android/data/com.yoryky.demo/cache/yoryky.txt

            FileOutputStream outputStream = new FileOutputStream(RED_LIST);
            outputStream.write(content.getBytes());
            outputStream.close();
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
