package com.hxs.xposedreddevil.service;

import android.Manifest;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.telephony.TelephonyManager;
import android.widget.Toast;

import com.hxs.xposedreddevil.entry.ChatRoom;
import com.hxs.xposedreddevil.utils.AppMD5Util;
import com.hxs.xposedreddevil.utils.Constant;
import com.hxs.xposedreddevil.utils.FileUtil;
import com.hxs.xposedreddevil.utils.MessageEvent;
import com.hxs.xposedreddevil.utils.ShellCommand;

import net.sqlcipher.database.SQLiteDatabase;
import net.sqlcipher.database.SQLiteDatabaseHook;

import org.greenrobot.eventbus.EventBus;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.litepal.crud.DataSupport;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.hxs.xposedreddevil.utils.Constant.COPY_WX_DATA_DB;
import static com.hxs.xposedreddevil.utils.Constant.WX_DB_DIR_PATH;
import static com.hxs.xposedreddevil.utils.Constant.WX_DB_FILE_NAME;
import static com.hxs.xposedreddevil.utils.Constant.WX_ROOT_PATH;
import static com.hxs.xposedreddevil.utils.Constant.WX_SP_UIN_PATH;
import static com.hxs.xposedreddevil.utils.Constant.currApkPath;
import static com.hxs.xposedreddevil.utils.Constant.uin;

public class GroupChatService extends IntentService {

    public GroupChatService() {
        super("GroupChatService");
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        TelephonyManager manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Iterator olditerator = null;
        String u = "";
        String imei = manager.getDeviceId();
        ShellCommand.shellCommand("chmod -R 777 " + WX_ROOT_PATH);
        Document doc = null;
        try {
            doc = Jsoup.parse(new File(WX_SP_UIN_PATH), "UTF-8");
            Elements elements = doc.select("int");
            Iterable it = (Iterable) elements;
            Iterator iterator = it.iterator();
            List destinationlist = new ArrayList();
            while (iterator.hasNext()) {
                Object element = iterator.next();
                Element its = (Element) element;
                if (its.attr("name").equals("_auth_uin")) {
                    destinationlist.add(element);
                }
            }
            it = destinationlist;
            for (olditerator = it.iterator(); olditerator.hasNext(); uin = u) {
                Object o = olditerator.next();
                u = ((Element) o).attr("value");
            }
            if (u.length() == 0) {
                Toast.makeText(this, "请登录微信后重试", Toast.LENGTH_SHORT).show();
            }
            Constant.dbPwd = AppMD5Util.getMD5Str(imei + u).substring(0, 7);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String uinEnc = AppMD5Util.getMD5Str("mm" + u);
        File dbDir = new File(WX_DB_DIR_PATH + uinEnc);
        List<File> fileList = FileUtil.searchFile(dbDir, WX_DB_FILE_NAME);
        for (int i = 0; i < fileList.size(); i++) {
            String copyFilePath = currApkPath + COPY_WX_DATA_DB;
            FileUtil.copyFile(fileList.get(i).getAbsolutePath(), copyFilePath);
//            OpenMMDB(new File("/storage/emulated/0/wx_data.db"), Constant.dbPwd);
            OpenMMDB(new File(copyFilePath), Constant.dbPwd);
        }
    }

    private void OpenMMDB(File file, String pwd) {
        SQLiteDatabase.loadLibs(this);
        SQLiteDatabaseHook hook = new SQLiteDatabaseHook() {

            @Override
            public void preKey(SQLiteDatabase database) {

            }

            @Override
            public void postKey(SQLiteDatabase database) {
                database.rawExecSQL("PRAGMA cipher_migrate;"); // 兼容2.0的数据库
            }
        };
        SQLiteDatabase db = null;
        try {
            db = SQLiteDatabase.openOrCreateDatabase(file, pwd, null, hook);
            EventBus.getDefault().post(openChatRoomTable(db));
        } catch (Exception e) {
            EventBus.getDefault().post(new MessageEvent("error"));
            Toast.makeText(this, "获取群聊列表失败", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    private Map<String,String> openChatRoomTable(SQLiteDatabase db) {
        Map<String,String> map = new HashMap<>();
        Cursor cursor = db.rawQuery("select * from chatroom ", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                String name = cursor.getString(cursor.getColumnIndex("chatroomname"));
                String displayname = cursor.getString(cursor.getColumnIndex("displayname"));
                map.put(name,displayname);
            }
        }
        cursor.close();
        return map;
    }
}
