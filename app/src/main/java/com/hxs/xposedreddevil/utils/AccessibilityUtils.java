package com.hxs.xposedreddevil.utils;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.os.Build;
import android.view.accessibility.AccessibilityNodeInfo;

import androidx.core.view.accessibility.AccessibilityNodeInfoCompat;

import java.util.List;

/**
 * Created by Administrator on 2019/1/8.
 */

public class AccessibilityUtils {

    @SuppressLint("ObsoleteSdkInt")
    public static AccessibilityNodeInfo findNodeInfosById(AccessibilityNodeInfo nodeInfo, String resId) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
                List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
                if (list != null && !list.isEmpty()) {
                    return list.get(0);
                }
            } else {
                AccessibilityNodeInfoCompat compat = new AccessibilityNodeInfoCompat(nodeInfo);
                List<AccessibilityNodeInfoCompat> list = compat.findAccessibilityNodeInfosByViewId(resId);
                if (list != null && !list.isEmpty()) {
                    return (AccessibilityNodeInfo) list.get(0).getInfo();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

    public static AccessibilityNodeInfo findNodeInfosByIdLast(AccessibilityNodeInfo nodeInfo, String resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR2) {
            List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByViewId(resId);
            if (list != null && !list.isEmpty()) {
                return list.get(list.size() - 1);
            }
        } else {
            AccessibilityNodeInfoCompat compat = new AccessibilityNodeInfoCompat(nodeInfo);
            List<AccessibilityNodeInfoCompat> list = compat.findAccessibilityNodeInfosByViewId(resId);
            if (list != null && !list.isEmpty()) {
                return (AccessibilityNodeInfo) list.get(list.size() - 1).getInfo();
            }
        }
        return null;
    }

    /**
     * 通过文本查找
     */
    public static AccessibilityNodeInfo findNodeInfosByText(AccessibilityNodeInfo nodeInfo, String text) {
        List<AccessibilityNodeInfo> list = nodeInfo.findAccessibilityNodeInfosByText(text);
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * 通过关键字查找
     */
    public static AccessibilityNodeInfo findNodeInfosByTexts(AccessibilityNodeInfo nodeInfo, String... texts) {
        for (String key : texts) {
            AccessibilityNodeInfo info = findNodeInfosByText(nodeInfo, key);
            if (info != null) {
                return info;
            }
        }
        return null;
    }

    /**
     * EditText setText
     */
    public static void setText(AccessibilityNodeInfo input, String text) {
      /*  AccessibilityNodeInfoCompat compat = new AccessibilityNodeInfoCompat(input);
        if (Build.VERSION.SDK_INT > 21) {
            Bundle arguments = new Bundle();
            arguments.putCharSequence(AccessibilityNodeInfo.ACTION_ARGUMENT_SET_TEXT_CHARSEQUENCE, text);
            compat.performAction(AccessibilityNodeInfo.ACTION_SET_TEXT, arguments);
        }else{
            ClipboardManager clipboard = (ClipboardManager) Global.getContext().getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = ClipData.newPlainText("text", text);
            clipboard.setPrimaryClip(clip);
            compat.performAction(AccessibilityNodeInfo.ACTION_FOCUS);
            compat.performAction(AccessibilityNodeInfo.ACTION_PASTE);
        }*/
    }

    /**
     * 返回主界面事件
     */
    public static void performHome(AccessibilityService service) {
        if (service == null) {
            return;
        }
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_HOME);
    }

    /**
     * 返回事件
     */
    public static void performBack(AccessibilityService service) {
        if (service == null) {
            return;
        }
        service.performGlobalAction(AccessibilityService.GLOBAL_ACTION_BACK);
    }

    /**
     * 点击事件
     */
    public static void performClick(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        if (nodeInfo.isClickable()) {
            nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        } else {
            performClick(nodeInfo.getParent());
        }
    }

}