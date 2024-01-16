package com.hxs.xposedreddevil.base;

import android.Manifest;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Layout;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.utils.MultiprocessSharedPreferences;

import java.util.ArrayList;


/**
 * Created by fujiayi on 2017/9/13.
 * <p>
 * 此类 底层UI实现 无SDK相关逻辑
 */

public class BaseActivity extends AppCompatActivity{

    public SharedPreferences sharedPreferences;

    /*
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialShare(); // 初始化UI
    }


    private void initialShare() {
        MultiprocessSharedPreferences.setAuthority("com.hxs.xposedreddevil.provider");
        sharedPreferences =
                MultiprocessSharedPreferences.getSharedPreferences(this, "xr", MODE_PRIVATE);
    }

}
