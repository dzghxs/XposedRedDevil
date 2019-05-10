package com.hxs.xposedreddevil.hook;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import de.robv.android.xposed.XC_MethodHook;
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
                    Class<?> hookClass = null,hookClassMoney = null,hookClassIcon = null;
                    hookClass = cl.loadClass("com.qennnsad.aknkaksd.data.bean.room.PrivateLimitBean"); // 获取Class
                    XposedHelpers.findAndHookMethod(hookClass
                            , "getPreview_time",
                            new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    Object o = param.getResult();
                                    log("修改获得的剩余时间-----》" + o);
                                    param.setResult(999999);
                                }
                            });
                    hookClassMoney = cl.loadClass("com.qennnsad.aknkaksd.data.bean.me.UserMoney");
                    XposedHelpers.findAndHookMethod(hookClassMoney
                            , "getBeanbalance",
                            new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    Object o = param.getResult();
                                    log("修改获得的coin-----》" + o);
                                    param.setResult(999999);
                                }
                            });
                    XposedHelpers.findAndHookMethod(hookClassMoney
                            , "getCoinbalance",
                            new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    Object o = param.getResult();
                                    log("修改获得的coin-----》" + o);
                                    param.setResult(999999);
                                }
                            });
                    XposedHelpers.findAndHookMethod(hookClassMoney
                            , "getPointbalance",
                            new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    Object o = param.getResult();
                                    log("修改获得的coin-----》" + o);
                                    param.setResult(999999);
                                }
                            });
                    hookClassIcon = cl.loadClass("com.qennnsad.aknkaksd.data.bean.CurrencyRankItem");
                    XposedHelpers.findAndHookMethod(hookClassMoney
                            , "getCoin",
                            new XC_MethodHook() {
                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    Object o = param.getResult();
                                    log("修改获得的coin-----》" + o);
                                    param.setResult("999999");
                                }
                            });
                }
            });
        }
    }
}
