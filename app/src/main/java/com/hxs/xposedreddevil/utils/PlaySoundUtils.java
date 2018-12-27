package com.hxs.xposedreddevil.utils;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaPlayer;
import android.os.Environment;

import java.io.File;
import java.io.IOException;

public class PlaySoundUtils {
    /**
     * 播放音频
     *
     */
    public static void Play(){
        MediaPlayer mediaPlayer=new MediaPlayer();
        try {
            //设置文件路径
            mediaPlayer.setDataSource(Environment.getExternalStorageDirectory().toString() + "/xposedreddevil/lucky_sound.mp3");
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (IOException e) {
            System.out.println("错误");
            e.printStackTrace();
        }
    }

}
