package com.hxs.xposedreddevil;

import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.hook.RedHook;
import com.hxs.xposedreddevil.hook.RedHook673;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;
import static de.robv.android.xposed.XposedBridge.log;

public class RedDevil implements IXposedHookLoadPackage {

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        log("微信版本:" + PropertiesUtils.getValue(RED_FILE, "wechatversion", ""));
        if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("")) {
            RedHook673.getInstance().init(lpparam);
        } else if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("7.0.0")) {
            RedHook.getInstance().init(lpparam);
        } else if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("6.7.3")) {
            RedHook673.getInstance().init(lpparam);
        }
    }
}