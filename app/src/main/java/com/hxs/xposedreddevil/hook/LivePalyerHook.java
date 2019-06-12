package com.hxs.xposedreddevil.hook;

import android.annotation.SuppressLint;
import android.app.AndroidAppHelper;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.hxs.xposedreddevil.R;

import java.lang.reflect.Field;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.log;

public class LivePalyerHook {

    private XC_LoadPackage.LoadPackageParam classLoader;

    public LivePalyerHook() {
    }

    public void init(XC_LoadPackage.LoadPackageParam classLoader) {
        if (this.classLoader == null) {
            this.classLoader = classLoader;
            hook(classLoader);
        }
    }

    public static LivePalyerHook getInstance() {
        return LivePalyerHookHolder.instance;
    }

    private static class LivePalyerHookHolder {
        @SuppressLint("StaticFieldLeak")
        private static final LivePalyerHook instance = new LivePalyerHook();
    }

    private void hook(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.equals("com.qennnsad.aknkaksd")) {
            log("拦截到直播");
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    final ClassLoader cl = ((Context) param.args[0]).getClassLoader(); // 获取ClassLoader
                    Class<?> hookClass = null, hookClasslevel = null;
                    hookClass = cl.loadClass("com.qennnsad.aknkaksd.data.bean.room.PrivateLimitBean"); // 获取Class
                    XposedHelpers.findAndHookMethod(hookClass
                            , "getPreview_time",
                            new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    Object o = param.getResult();
                                    param.setResult(999999);
                                }
                            });
                    hookClass = cl.loadClass("com.qennnsad.aknkaksd.data.bean.room.PrivateLimitBean"); // 获取Class
                    XposedHelpers.findAndHookMethod(hookClass
                            , "getPlid",
                            new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    Object o = param.getResult();
                                    log("获取pid---》" + o);
                                    param.setResult(1);
                                }
                            });
                    hookClasslevel = cl.loadClass("com.qennnsad.aknkaksd.presentation.ui.room.player.player.privatedialog.PrivateRoomDialogFragment");
                    XposedHelpers.findAndHookMethod(hookClasslevel
                            , "onCreateView", LayoutInflater.class, ViewGroup.class, Bundle.class,
                            new XC_MethodReplacement() {
                                @Override
                                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                                    return LayoutInflater.from(AndroidAppHelper.currentApplication().getApplicationContext()).inflate(R.layout.replace_layout,null);
                                }
                            }
                    );
                    XposedHelpers.findAndHookMethod(hookClasslevel
                            , "a", View.class,
                            new XC_MethodReplacement() {
                                @Override
                                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                                    return "";
                                }
                            }
                    );
                    XposedHelpers.findAndHookMethod(hookClasslevel
                            , "b",
                            new XC_MethodReplacement() {
                                @Override
                                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                                    return "";
                                }
                            }
                    );
                }
            });
        }
    }
}
