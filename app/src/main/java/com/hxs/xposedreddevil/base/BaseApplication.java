package com.hxs.xposedreddevil.base;

import android.app.Application;
import android.os.Environment;

import com.tencent.bugly.Bugly;

import java.io.File;

import skin.support.SkinCompatManager;
import skin.support.design.app.SkinMaterialViewInflater;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "c296cae0b8", false);
        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
    }
}
