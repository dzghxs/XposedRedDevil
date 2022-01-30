package com.hxs.xposedreddevil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.hook.RedHook;
import com.hxs.xposedreddevil.hook.RevokeMsgHook;
import com.hxs.xposedreddevil.utils.AppMD5Util;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;
import static com.tencent.bugly.Bugly.applicationContext;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class RedDevil implements IXposedHookLoadPackage {

    private int die_count = 0;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        System.out.println("微信版本:" + PropertiesUtils.getValue(RED_FILE, "wechatversion", ""));
        try {
            Class<?> ContextClass = findClass("android.content.ContextWrapper", lpparam.classLoader);
            findAndHookMethod(ContextClass, "getApplicationContext", new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    if (applicationContext != null)
                        return;
                    applicationContext = (Context) param.getResult();
//                    log("微信状态：" + AppMD5Util.isRunning(applicationContext, "com.tencent.mm"));
                    if (!PropertiesUtils.getValue(RED_FILE, "openwechat", "2").equals("2")) {
                        if (!AppMD5Util.isRunning(applicationContext, "com.tencent.mm")) {
                            if (die_count == 10) {
                                Uri uri = Uri.parse("weixin://");
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                applicationContext.startActivity(intent);
                                die_count = 0;
                            }
                            die_count += 1;
                        }
                    }
                }
            });
        } catch (Throwable t) {
//            log("微信状态错误：" + t);
        }
        RedHook.getInstance().init(lpparam);
        RevokeMsgHook.getInstance().init(lpparam);
    }
}