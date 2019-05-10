package com.hxs.xposedreddevil.greendao.helper;

/**
 * Created by zed on 2018/5/8.
 */

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.hxs.xposedreddevil.greendao.DaoMaster;
import com.hxs.xposedreddevil.greendao.WxChatInvokeMsgDao;

import org.greenrobot.greendao.database.Database;


public class MyOpenHelper extends DaoMaster.OpenHelper {

    public MyOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory) {
        super(context, name, factory);
    }

    /**
     * 数据库升级
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(Database db, int oldVersion, int newVersion) {
        //操作数据库的更新 有几个表升级都可以传入到下面

        Log.i("version", oldVersion + "---先前和更新之后的版本---" + newVersion);
        if (oldVersion < newVersion) {
            Log.i("version", oldVersion + "---先前和更新之后的版本---" + newVersion);
            MigrationHelper.getInstance().migrate(db, WxChatInvokeMsgDao.class);
            //更改过的实体类(新增的不用加)   更新UserDao文件 可以添加多个  XXDao.class 文件
//             MigrationHelper.getInstance().migrate(db, UserDao.class,XXDao.class);
        }



        //MigrationHelper.getInstance().migrate(db,StudentDao.class);
    }

}
