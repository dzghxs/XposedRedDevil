package com.hxs.xposedreddevil.base;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.control.MainHandlerConstant;

import java.util.ArrayList;


/**
 * Created by fujiayi on 2017/9/13.
 * <p>
 * 此类 底层UI实现 无SDK相关逻辑
 */

public class BaseActivity extends AppCompatActivity implements MainHandlerConstant {
    protected Handler mainHandler;


    /*
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mainHandler = new Handler() {
            /*
             * @param msg
             */
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                handle(msg);
            }

        };
        initialView(); // 初始化UI
    }


    private void initialView() {

    }

    protected void handle(Message msg) {
        int what = msg.what;
        switch (what) {
            case PRINT:
                print(msg);
                break;
            case UI_CHANGE_INPUT_TEXT_SELECTION:
                break;
            case UI_CHANGE_SYNTHES_TEXT_SELECTION:
                break;
            default:
                break;
        }
    }

    protected void toPrint(String str) {
        Message msg = Message.obtain();
        msg.obj = str;
        mainHandler.sendMessage(msg);
    }

    private void print(Message msg) {
        String message = (String) msg.obj;
        if (message != null) {
            scrollLog(message);
        }
    }

    private void scrollLog(String message) {
        Spannable colorMessage = new SpannableString(message + "\n");
        colorMessage.setSpan(new ForegroundColorSpan(0xff0000ff), 0, message.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
    }

}
