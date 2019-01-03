package com.hxs.xposedreddevil.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.List;

public class PackageManagerUtil {

    /**
     * 获取微信版本号等信息
     * @param context
     * @return
     */
    public static String getItems(Context context) {
        List<PackageInfo> packages = context.getPackageManager()
                .getInstalledPackages(PackageManager.GET_ACTIVITIES);
        String items = "";
        for (int i = 0; i < packages.size(); i++) {
            if(packages.get(i).packageName.equals("com.tencent.mm")){
                items = packages.get(i).versionName;
            }
        }
        return items;
    }
}
