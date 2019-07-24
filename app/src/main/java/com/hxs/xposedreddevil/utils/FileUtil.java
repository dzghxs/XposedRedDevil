package com.hxs.xposedreddevil.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {

    private static List<File> list = new ArrayList<>();

    public static List<File> searchFile(File file, String filename) {
        list.clear();
        recursiveSearchFile(file, filename);
        return list;
    }

    private static void recursiveSearchFile(File file, String filename) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (int i = 0; i < files.length; i++) {
                    recursiveSearchFile(files[i], filename);
                }
            }
        } else {
            if (filename.equals(file.getName())) {
                list.add(file);
            }
        }
    }

    public static void copyFile(String oldPath, String newPath) {
        try {
            FileChannel inputChannel = null;
            FileChannel outputChannel = null;
            try {
                inputChannel = new FileInputStream(new File(oldPath)).getChannel();
                outputChannel = new FileOutputStream(new File(newPath)).getChannel();
                outputChannel.transferFrom(inputChannel, 0, inputChannel.size());
            } finally {
                inputChannel.close();
                outputChannel.close();
            }
//            File oldfile = new File(oldPath);
//            if (oldfile.exists()) {
//                FileInputStream inputStream = new FileInputStream(oldfile);
//                FileOutputStream fileOutputStream = new FileOutputStream(newPath);
//                byte[] buffer = new byte[1024];
//                int byteRead = inputStream.read();
//                while (byteRead!=-1){
//                    fileOutputStream.write(buffer,0,byteRead);
//                    byteRead = inputStream.read(buffer);
//                }
//                inputStream.close();
//            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 创建一个临时目录，用于复制临时文件，如assets目录下的离线资源文件
    public static String createTmpDir(Context context) {
        String sampleDir = "baiduTTS";
        String tmpDir = Environment.getExternalStorageDirectory().toString() + "/" + sampleDir;
        if (!FileUtil.makeDir(tmpDir)) {
            tmpDir = context.getExternalFilesDir(sampleDir).getAbsolutePath();
            if (!FileUtil.makeDir(sampleDir)) {
                throw new RuntimeException("create model resources dir failed :" + tmpDir);
            }
        }
        return tmpDir;
    }

    public static boolean fileCanRead(String filename) {
        File f = new File(filename);
        return f.canRead();
    }

    public static boolean makeDir(String dirPath) {
        File file = new File(dirPath);
        if (!file.exists()) {
            return file.mkdirs();
        } else {
            return true;
        }
    }

    public static void copyFromAssets(AssetManager assets, String source, String dest, boolean isCover)
            throws IOException {
        File file = new File(dest);
        if (isCover || (!isCover && !file.exists())) {
            InputStream is = null;
            FileOutputStream fos = null;
            try {
                is = assets.open(source);
                String path = dest;
                fos = new FileOutputStream(path);
                byte[] buffer = new byte[1024];
                int size = 0;
                while ((size = is.read(buffer, 0, 1024)) >= 0) {
                    fos.write(buffer, 0, size);
                }
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } finally {
                        if (is != null) {
                            is.close();
                        }
                    }
                }
            }
        }
    }

}
