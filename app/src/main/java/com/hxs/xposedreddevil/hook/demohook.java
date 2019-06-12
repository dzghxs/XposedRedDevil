package com.hxs.xposedreddevil.hook;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

import org.json.JSONArray;

import java.util.ArrayList;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.log;

public class demohook {

    private XC_LoadPackage.LoadPackageParam classLoader;

    public demohook() {
    }

    public void init(XC_LoadPackage.LoadPackageParam classLoader) {
        if (this.classLoader == null) {
            this.classLoader = classLoader;
            hook(classLoader);
        }
    }

    public static demohook getInstance() {
        return demohookHolder.instance;
    }

    private static class demohookHolder {
        @SuppressLint("StaticFieldLeak")
        private static final demohook instance = new demohook();
    }

    private void hook(final XC_LoadPackage.LoadPackageParam lpparam) {
        // 被Hook操作的目标Android应用的包名，进行Hook操作的过滤
        String strPackageName = "com.one.tomato";
        if (lpparam.packageName.equals(strPackageName)) {
            log("Loaded App:" + lpparam.packageName);
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    final ClassLoader cl = ((Context) param.args[0]).getClassLoader(); // 获取ClassLoader
                    Class<?> hookClass = null;
                    hookClass = cl.loadClass("com.one.tomato.base.BaseApplication");
                    XposedHelpers.findAndHookMethod(hookClass, "d", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Object o = param.getResult();
                            log("打印的d值："+o);
                        }

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });
                    Class<?> hookClass2 = null;
                    hookClass2 = cl.loadClass("com.one.tomato.utils.NetWorkValidUtil");
                    XposedHelpers.findAndHookMethod(hookClass2, "b", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Object o = param.getResult();
                            log("打印的b值："+o);
                        }

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(true);
                        }
                    });
                    Class<?> hookClass3 = null;
                    hookClass3 = cl.loadClass("com.one.tomato.ui.papa.PapaTabFragment");
                    log("进了a方法");
                    XposedHelpers.findAndHookMethod(hookClass3, "a", boolean.class,
                            XposedHelpers.findClass("com.one.tomato.entity.LookTimes", cl)
                            , new XC_MethodReplacement() {
                                @Override
                                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                                    return "拦截";
                                }
                            });
                    XposedHelpers.findAndHookMethod(hookClass3, "q"
                            , new XC_MethodReplacement() {
                                @Override
                                protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                                    return "拦截";
                                }
                            });
                    Class<?> hookClass4 = null;
                    hookClass4 = cl.loadClass("com.one.tomato.entity.LastLookTimeBean");
                    log("进了a方法");
                    XposedHelpers.findAndHookMethod(hookClass4, "getLastTime", new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    Object o = param.getResult();
                                    log("打印的getLastTime值："+o);
                                }

                                @Override
                                protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                    param.setResult(999999);
                                }
                            });
                    XposedHelpers.findAndHookMethod(hookClass4, "getCountTime", new XC_MethodHook() {
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Object o = param.getResult();
                            log("打印的getCountTime值："+o);
                        }

                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            param.setResult(999999);
                        }
                    });
                }
            });
        }
    }

}
