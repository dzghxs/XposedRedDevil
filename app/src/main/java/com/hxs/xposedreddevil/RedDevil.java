package com.hxs.xposedreddevil;

import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.hook.LivePalyerHook;
import com.hxs.xposedreddevil.hook.RedHook;
import com.hxs.xposedreddevil.hook.RedHook673;
import com.hxs.xposedreddevil.hook.RedHook703;
import com.hxs.xposedreddevil.hook.RedHook704;
import com.hxs.xposedreddevil.hook.RedWithdrawHook;
import com.hxs.xposedreddevil.hook.SoulHook;
import com.hxs.xposedreddevil.hook.demohook;

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
        } else if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("7.0.4")) {
            RedHook704.getInstance().init(lpparam);
        } else if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("7.0.3")) {
            RedHook703.getInstance().init(lpparam);
        } else if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("7.0.0")) {
            RedHook.getInstance().init(lpparam);
        } else if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("6.7.3")) {
            RedHook673.getInstance().init(lpparam);
        }
        if(PropertiesUtils.getValue(RED_FILE, "withdraw", "2").equals("1")){
            RedWithdrawHook.getInstance().init(lpparam);
        }
    }
}