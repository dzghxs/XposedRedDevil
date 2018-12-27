package com.hxs.xposedreddevil.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import static android.content.ContentValues.TAG;

public class AssetsCopyTOSDcard {

    /***
     * 调用方式
     *
     * String path = Environment.getExternalStorageDirectory().toString() + "/" + "Tianchaoxiong/useso";
     String modelFilePath = "Model/seeta_fa_v1.1.bin";
     Assets2Sd(this, modelFilePath, path + "/" + modelFilePath);
     *
     * @param context
     * @param fileAssetPath assets中的目录
     * @param fileSdPath 要复制到sd卡中的目录
     */
    public static void Assets2Sd(Context context, String fileAssetPath, String fileSdPath){
        //测试把文件直接复制到sd卡中 fileSdPath完整路径
        File file = new File(fileSdPath);
        if (!file.exists()) {
            try {
                copyBigDataToSD(context, fileAssetPath, fileSdPath);
                Log.d(TAG, "************拷贝成功");
            } catch (IOException e) {
                Log.d(TAG, "************拷贝失败");
                e.printStackTrace();
            }
        } else {
            Log.d(TAG,"************文件夹存在,文件存在");
        }

    }
    public static void copyBigDataToSD(Context context, String fileAssetPath, String strOutFileName) throws IOException
    {
        InputStream myInput;
        OutputStream myOutput = new FileOutputStream(strOutFileName);
        myInput = context.getAssets().open(fileAssetPath);
        byte[] buffer = new byte[1024];
        int length = myInput.read(buffer);
        while(length > 0)
        {
            myOutput.write(buffer, 0, length);
            length = myInput.read(buffer);
        }

        myOutput.flush();
        myInput.close();
        myOutput.close();
    }
}
