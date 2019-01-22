package com.hxs.xposedreddevil.utils;

import java.security.MessageDigest;

public class AppMD5Util {

    public static String getMD5Str(String str){
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        byte[] bytes = messageDigest.digest();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bytes.length; i++) {
            if(Integer.toHexString(0xFF & bytes[i]).length()==1){
                sb.append("0").append(Integer.toHexString(0xFF & bytes[i]));
            }else{
                sb.append(Integer.toHexString(0xFF & bytes[i]));
            }
        }
        return sb.toString();
    }
}
