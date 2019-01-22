package com.hxs.xposedreddevil.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
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

}
