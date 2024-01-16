package com.hxs.xposedreddevil.base;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.tencent.bugly.Bugly;

import org.litepal.LitePal;

public class BaseApplication extends Application {

    private SQLiteDatabase db;
    public static BaseApplication instances;

    @Override
    public void onCreate() {
        super.onCreate();
        instances = this;
        Bugly.init(getApplicationContext(), "c296cae0b8", false);
        LitePal.initialize(this);
//        SkinCompatManager.withoutActivity(this)                         // 基础控件换肤初始化
//                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
//                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
//                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
//                .loadSkin();
    }

    public static BaseApplication getInstances() {
        return instances;
    }

     /* 通过 DaoMaster 的内部类 DevOpenHelper，你可以得到一个便利的 SQLiteOpenHelper 对象。
可能你已经注意到了，你并不需要去编写「CREATE TABLE」这样的 SQL 语句，因为 greenDAO 已经帮你做了。 注意：默认的 DaoMaster.DevOpenHelper 会在数据库升级时，删除所有的表，意味着这将导致数据的丢失。*/

}
