package com.hxs.xposedreddevil.hook;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.text.Html;
import android.text.TextUtils;

import com.hxs.xposedreddevil.utils.MultiprocessSharedPreferences;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;


public class RevokeMsgHook {
    private static Map<Long, Object> msgCacheMap = new HashMap<>();

    private static Object storageInsertObject;
    private static String insertMethodName;

    private static boolean disableRevoke;
    private XC_LoadPackage.LoadPackageParam classLoader;

    private static SharedPreferences share;

    private static final String SQLITE_DATABASE_CLAZZ = "com.tencent.wcdb.database.SQLiteDatabase";

    private RevokeMsgHook() {

    }

    public static RevokeMsgHook getInstance() {
        return RevokeMsgHook.ExdeviceRankHookHolder.instance;
    }

    private static class ExdeviceRankHookHolder {
        @SuppressLint("StaticFieldLeak")
        private static final RevokeMsgHook instance = new RevokeMsgHook();
    }

    public void init(XC_LoadPackage.LoadPackageParam classLoader) {
        if (this.classLoader == null) {
            this.classLoader = classLoader;
            hook(classLoader);
        }
    }

    private void hook(XC_LoadPackage.LoadPackageParam classLoader) {
        try {
            if (classLoader.packageName.contains("com.tencent.mm")) {
                XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {

                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        super.afterHookedMethod(param);
                        Context context = (Context) param.args[0];
                        MultiprocessSharedPreferences.setAuthority("com.hxs.xposedreddevil.provider");
                        share = MultiprocessSharedPreferences.getSharedPreferences(
                                context,
                                "xr",
                                MODE_PRIVATE
                        );
                        Class clazzs = XposedHelpers.findClass(SQLITE_DATABASE_CLAZZ, context.getClassLoader());
                        XposedHelpers.findAndHookMethod(clazzs, "updateWithOnConflict",
                                String.class, ContentValues.class, String.class, String[].class, int.class,
                                new XC_MethodHook() {
                                    @Override
                                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                        if (param.args[0].equals("message")) {
                                            ContentValues values = ((ContentValues) param.args[1]);
                                            if (isRecalledMessage(values)) {
                                                reload();
                                                if (disableRevoke) {
                                                    handleMessageRecall(values);
                                                    param.setResult(1);
                                                }
                                            }
                                        }
                                        super.beforeHookedMethod(param);
                                    }
                                });
                        try {
                            Class clazz = XposedHelpers.findClass("com.tencent.wcdb.database.SQLiteDatabase", context.getClassLoader());
                            XposedHelpers.findAndHookMethod(clazz, "delete",
                                    String.class, String.class, String[].class,
                                    new XC_MethodHook() {
                                        @Override
                                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                            String[] media = {"ImgInfo2", "voiceinfo", "videoinfo2", "WxFileIndex2"};
                                            if (disableRevoke && Arrays.asList(media).contains(param.args[0])) {
                                                param.setResult(1);
                                            }
                                            super.beforeHookedMethod(param);
                                        }
                                    });
                        } catch (Error | Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            XposedHelpers.findAndHookMethod(File.class, "delete",
                                    new XC_MethodHook() {
                                        @Override
                                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                            String path = ((File) param.thisObject).getAbsolutePath();
                                            if (disableRevoke &&
                                                    (path.contains("/image2/") || path.contains("/voice2/") || path.contains("/video/")))
                                                param.setResult(true);
                                            super.beforeHookedMethod(param);
                                        }
                                    });
                        } catch (Error | Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            Class insertClazz = getInsertClazz(context.getClassLoader());
                            if (insertClazz != null) {
                                insertMethodName = getInsertMethodName(insertClazz);
                                XposedBridge.hookAllMethods(insertClazz, insertMethodName,
                                        new XC_MethodHook() {
                                            @Override
                                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                                storageInsertObject = param.thisObject;
                                                if (param.args.length == 2) {
                                                    Object msg = param.args[0];
                                                    if (msg == null) {
                                                        return;
                                                    }
                                                    try {
                                                        long msgId = XposedHelpers.getLongField(msg, "field_msgId");
                                                        msgCacheMap.put(msgId, msg);
                                                    } catch (Throwable e) {
                                                        e.printStackTrace();
                                                    }
                                                }
                                                super.afterHookedMethod(param);
                                            }
                                        });
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        } catch (Error | Exception e) {
            e.printStackTrace();
        }
    }

    private static void reload() {
        if (share.getString("withdraw", "2").equals("2")) {
            disableRevoke = false;
        } else {
            disableRevoke = true;
        }
    }

    private static boolean isRecalledMessage(ContentValues values) {
        if (values == null) {
            return false;
        }
        try {
            String content = values.getAsString("content");
            if (!TextUtils.isEmpty(content)
                    && values.getAsInteger("type") == 10000) {
                if (!content.contains("你撤回了一条消息") && !content.contains("You've recalled a message")) {
                    return content.contains("\" 撤回了一条消息") || content.contains("has recalled a message");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    private static Class getInsertClazz(ClassLoader classLoader) {
        try {
            Class pluginMessengerFoundationClazz = XposedHelpers.findClass("com.tencent.mm.plugin.messenger.foundation.PluginMessengerFoundation", classLoader);
            Field[] fields = pluginMessengerFoundationClazz.getDeclaredFields();
            Field tempField = null;
            // fields 数量最多的
            for (Field field : fields) {
                if (field.getType().getName().startsWith("com.tencent.mm.plugin.messenger.foundation")) {
                    if (tempField == null ||
                            tempField.getType().getDeclaredFields().length < field.getType().getDeclaredFields().length) {
                        tempField = field;
                    }
                }
            }
            // fields 里构造方法只有一个且参数为三个的
            if (tempField != null) {
                Field[] fieldFields = tempField.getType().getDeclaredFields();
                for (Field fieldField : fieldFields) {
                    Constructor[] constructors = fieldField.getType().getConstructors();
                    if (constructors.length == 1 && constructors[0].getParameterTypes().length == 3) {
                        return fieldField.getType();
                    }
                }
            }
        } catch (Error | Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    private static String getInsertMethodName(Class insertClass) {
        Method[] methods = insertClass.getDeclaredMethods();
        for (Method method : methods) {
            if (method.getParameterTypes().length == 2
                    && method.getParameterTypes()[1] == boolean.class
                    && method.getReturnType() == long.class) {
                return method.getName();
            }
        }
        return "";
    }

    private static void handleMessageRecall(ContentValues contentValues) {
        long msgId = contentValues.getAsLong("msgId");
        Object msg = msgCacheMap.get(msgId);
        if (msg != null) {
            long createTime = XposedHelpers.getLongField(msg, "field_createTime");
            XposedHelpers.setIntField(msg, "field_type", contentValues.getAsInteger("type"));
            XposedHelpers.setObjectField(msg, "field_content",
                    contentValues.getAsString("content") + "(已经让我留下了)");
            XposedHelpers.setLongField(msg, "field_createTime", createTime + 1L);
            XposedHelpers.callMethod(storageInsertObject, insertMethodName, msg, false);
        }
    }
}
