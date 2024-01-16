package com.hxs.xposedreddevil;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.hxs.xposedreddevil.hook.RedHook;
import com.hxs.xposedreddevil.hook.RevokeMsgHook;
import com.hxs.xposedreddevil.utils.AppMD5Util;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class RedDevil implements IXposedHookLoadPackage {

    private int die_count = 0;

    @Override
    public void handleLoadPackage(final XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        RedHook.getInstance().init(lpparam);
        RevokeMsgHook.getInstance().init(lpparam);
    }
}