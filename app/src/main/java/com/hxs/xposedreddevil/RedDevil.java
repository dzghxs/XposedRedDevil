package com.hxs.xposedreddevil;

import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.hook.RedHook;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.hxs.xposedreddevil.ui.MainActivity.RED_FILE;
import static de.robv.android.xposed.XposedBridge.log;

public class RedDevil implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        log("微信版本:"+PropertiesUtils.getValue(RED_FILE, "wechatversion", ""));
        if(PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("")){
            RedHook.getInstance().init(lpparam);
            return;
        }
        if(PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("7.0.0")){
            RedHook.getInstance().init(lpparam);
        }
//        LiveHook.getInstance().init(lpparam);
    }
}