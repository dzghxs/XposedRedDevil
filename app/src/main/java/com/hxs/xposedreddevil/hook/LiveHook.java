package com.hxs.xposedreddevil.hook;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;

import com.hxs.xposedreddevil.model.UserAccout;

import java.lang.reflect.Field;

import de.robv.android.xposed.IXposedHookInitPackageResources;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_InitPackageResources;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class LiveHook {

    private XC_LoadPackage.LoadPackageParam classLoader;

    public LiveHook() {
    }

    public void init(XC_LoadPackage.LoadPackageParam classLoader) {
        if (this.classLoader == null) {
            this.classLoader = classLoader;
            hook(classLoader);
        }
    }

    public static LiveHook getInstance() {
        return LiveHookHolder.instance;
    }

    private static class LiveHookHolder {
        @SuppressLint("StaticFieldLeak")
        private static final LiveHook instance = new LiveHook();
    }

    private void hook(final XC_LoadPackage.LoadPackageParam lpparam){
        if(lpparam.packageName.equals("com.tencent.livethree")){
            XposedHelpers.findAndHookMethod("com.tencent.livethree.MyApplication", lpparam.classLoader, "f",new XC_MethodHook() {
                        //进行hook操作
                        @Override
                        protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            Field field = XposedHelpers.findField(param.thisObject.getClass(), "b");
                            log("---------------->直播"+field.get(param.thisObject).toString()+"");
                        }
                    });

        }
    }
}
