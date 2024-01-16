package com.hxs.xposedreddevil.service;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

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
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import static com.hxs.xposedreddevil.utils.Constant.COPY_WX_DATA_DB;
import static com.hxs.xposedreddevil.utils.Constant.WX_DB_DIR_PATH;
import static com.hxs.xposedreddevil.utils.Constant.WX_DB_FILE_NAME;
import static com.hxs.xposedreddevil.utils.Constant.WX_ROOT_PATH;
import static com.hxs.xposedreddevil.utils.Constant.WX_SP_UIN_PATH;
import static com.hxs.xposedreddevil.utils.Constant.currApkPath;
import static com.hxs.xposedreddevil.utils.Constant.uin;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;

public class GroupChatService extends IntentService {

    public GroupChatService() {
        super("GroupChatService");
    }


    public static String getDeviceId(Context context) {
        final int targetSdkVersion = context.getApplicationInfo().targetSdkVersion;
        if (targetSdkVersion > Build.VERSION_CODES.P && Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            return getUniqueID(context);
        } else {
            return getTelId(context);
        }
    }

    private static String getTelId(Context context) {
        final TelephonyManager manager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            return null;
        }
        return manager.getDeviceId();
    }

    private static String getUniqueID(Context context) {
        String id = null;
        final String androidId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        if (!TextUtils.isEmpty(androidId) && !"9774d56d682e549c".equals(androidId)) {
            try {
                UUID uuid = UUID.nameUUIDFromBytes(androidId.getBytes("utf8"));
                id = uuid.toString();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }

        if (TextUtils.isEmpty(id)) {
            id = getUUID(context);
        }

        return TextUtils.isEmpty(id) ? UUID.randomUUID().toString() : id;
    }

    private static String getUUID(Context context) {
        String serial = null;

        String m_szDevIDShort = "35" +
                Build.BOARD.length() % 10 + Build.BRAND.length() % 10 +

                ((null != Build.CPU_ABI) ? Build.CPU_ABI.length() : 0) % 10 +

                Build.DEVICE.length() % 10 + Build.DISPLAY.length() % 10 +

                Build.HOST.length() % 10 + Build.ID.length() % 10 +

                Build.MANUFACTURER.length() % 10 + Build.MODEL.length() % 10 +

                Build.PRODUCT.length() % 10 + Build.TAGS.length() % 10 +

                Build.TYPE.length() % 10 + Build.USER.length() % 10; //13 位

        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                        return null;
                    }
                    serial = Build.getSerial();
                } else {
                    serial = Build.SERIAL;
                }
                //API>=9 使用serial号
                return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
            } catch (Exception exception) {
                serial = "serial" + UUID.randomUUID().toString(); // 随便一个初始化
            }
        } else {
            serial = Build.UNKNOWN + UUID.randomUUID().toString(); // 随便一个初始化
        }

        //使用硬件信息拼凑出来的15位号码
        return new UUID(m_szDevIDShort.hashCode(), serial.hashCode()).toString();
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
//        @SuppressLint({"MissingPermission", "HardwareIds"}) String imei = getDeviceId(this);
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
//            Constant.dbPwd = AppMD5Util.getMD5Str(imei + u).substring(0, 7).toLowerCase();
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
                database.execSQL("PRAGMA cipher_page_size = 1024");
                database.execSQL("PRAGMA kdf_iter = 64000");
                database.execSQL("PRAGMA cipher_hmac_algorithm = HMAC_SHA1");
                database.execSQL("PRAGMA cipher_kdf_algorithm = PBKDF2_HMAC_SHA1");
            }
        };
    }

}
