package com.hxs.xposedreddevil.base;

import android.app.Application;
import android.os.Environment;

import com.tencent.bugly.Bugly;

import java.io.File;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "c296cae0b8", false);
    }
}
