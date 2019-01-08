package com.hxs.xposedreddevil.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.hook.RedHook;
import com.hxs.xposedreddevil.hook.RedHook673;
import com.hxs.xposedreddevil.utils.PackageManagerUtil;

import java.util.List;

import static com.hxs.xposedreddevil.ui.MainActivity.RED_FILE;

public class AcxiliaryRedService extends AccessibilityService {
    /**
     * 键盘锁的对象
     */
    private KeyguardManager.KeyguardLock kl;

    /**
     * 是否有打开微信页面
     */
    private boolean isOpenPage = false;

    /**
     * 是否点击了红包
     */
    private boolean isOpenRP = false;

    /**
     * 是否点击了开按钮，打开了详情页面
     */
    private boolean isOpenDetail = false;

    /**
     * 红包
     */
    private AccessibilityNodeInfo rpNode;

    /**
     * 微信几个页面的包名+地址。用于判断在哪个页面
     */
    private String LAUCHER = "com.tencent.mm.ui.LauncherUI";
    private String LUCKEY_MONEY_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    private String LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";

    @Override
    public void onAccessibilityEvent(final AccessibilityEvent event) {
        if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("")) {
            LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
        } else if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("7.0.0")) {
            LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
        } else if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("6.7.3")) {
            LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
        }
        //接收事件
        int eventType = event.getEventType();
        if(PropertiesUtils.getValue(RED_FILE, "rednorootmain", "2").equals("2")){
            return;
        }
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED: {
                //通知栏事件
                List<CharSequence> texts = event.getText();
                if (texts.isEmpty())
                    break;
                for (CharSequence text : texts) {
                    String content = text.toString();
                    //通过微信红包这个关键词来判断是否红包。（如果有个朋友取名叫微信红包的话。。。）
                    int i = text.toString().indexOf("[微信红包]");
                    //如果不是微信红包，则不需要做其他工作了
                    if (i == -1)
                        break;
                    if (!TextUtils.isEmpty(content)) {
                        if (isScreenLocked()) {
                            //如果屏幕被锁，就解锁
                            wakeAndUnlock();

                            //打开微信的页面
                            openWeichaPage(event);
                        } else {
                            //屏幕是亮的
                            //打开微信的页面
                            openWeichaPage(event);
                        }
                    }
                }
                break;
            }
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED: {
                //监测到窗口变化。
                if (isOpenPage) {
                    //如果是本程序打开了微信页面，那就执行去找红包
                    isOpenPage = false;
                    String className = event.getClassName().toString();
                    //是否微信聊天页面的类
                    if (className.equals(LAUCHER)) {
                        findStuff();
                    }
                }

                if (isOpenRP && LUCKEY_MONEY_RECEIVER.equals(event.getClassName().toString())) {
                    //如果打开了抢红包页面
                    AccessibilityNodeInfo rootNode1 = getRootInActiveWindow();
                    if (findOpenBtn(rootNode1)) {
                        //如果找到按钮
                        isOpenDetail = true;
                    }
                    isOpenRP = false;
                }

                if (isOpenDetail && LUCKEY_MONEY_DETAIL.equals(event.getClassName().toString())) {
                    //打开了红包详情页面，看下抢了多少钱
                    findInDatail(getRootInActiveWindow());

                    isOpenDetail = false;
                }
                break;
            }
        }
        //释放一下资源。
        releese();
    }

    /**
     *  在红包详情页面寻找抢到多少钱。
     *  实际上不关心的童鞋可以不写这个方法了。
     */
    private boolean findInDatail(AccessibilityNodeInfo rootNode) {
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            AccessibilityNodeInfo nodeInfo = rootNode.getChild(i);
            if ("android.widget.TextView".equals(nodeInfo.getClassName().toString())) {
                if ("元".equals(nodeInfo.getText().toString())) {
                    final float momeny = Float.parseFloat(rootNode.getChild(i - 1).getText().toString());
                    return true;
                }
            }
            if (findInDatail(nodeInfo)) {
                return true;
            }
        }
        return false;
    }

    /**
     *   遍历找东西
     */
    private void findStuff() {
        AccessibilityNodeInfo rootNode = getRootInActiveWindow();

        if (findRP(rootNode)) {
            isOpenRP = true;
        }
        if (rpNode != null) {
            rpNode.performAction(AccessibilityNodeInfo.ACTION_CLICK);
        }
    }

    /**
     *  在聊天页面迭代找红包
     */
    private boolean findRP(AccessibilityNodeInfo rootNode) {
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            AccessibilityNodeInfo nodeInfo = rootNode.getChild(i);
            if (nodeInfo == null) {
//                Log.d("mylog", "--------nodeinfo == null");
                continue;
            }
            if ("android.widget.TextView".equals(nodeInfo.getClassName())) {
                if ("微信红包".equals(nodeInfo.getText().toString())) {
                    isOpenRP = true;
                    nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                    return true;
                }
            }

            if (findRP(nodeInfo)) {
                if ("android.widget.LinearLayout".equals(nodeInfo.getClassName())) {
                    rpNode = nodeInfo;
                    return true;
                }
            }
        }
        return false;
    }

    //
    private boolean findOpenBtn(AccessibilityNodeInfo rootNode) {
        for (int i = 0; i < rootNode.getChildCount(); i++) {
            AccessibilityNodeInfo nodeInfo = rootNode.getChild(i);
            if ("android.widget.Button".equals(nodeInfo.getClassName())) {
                nodeInfo.performAction(AccessibilityNodeInfo.ACTION_CLICK);
                return true;
            }
            findOpenBtn(nodeInfo);
        }
        return false;
    }

    //打开微信聊天页面
    private void openWeichaPage(AccessibilityEvent event) {
        if (event.getParcelableData() != null && event.getParcelableData() instanceof Notification) {
            //得到通知的对象
            Notification notification = (Notification) event.getParcelableData();

            isOpenPage = true;

            //打开通知栏的intent，即打开对应的聊天界面
            PendingIntent pendingIntent = notification.contentIntent;
            try {
                pendingIntent.send();
            } catch (PendingIntent.CanceledException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 系统是否在锁屏状态
     */
    private boolean isScreenLocked() {
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();//如果为true，则表示屏幕“亮”了，否则屏幕“暗”了。

        return !isScreenOn;
    }

    /**
     * 解锁
     */
    private void wakeAndUnlock() {
        //获取电源管理器对象
        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);

        //获取PowerManager.WakeLock对象，后面的参数|表示同时传入两个值，最后的是调试用的Tag
        @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.SCREEN_BRIGHT_WAKE_LOCK, "bright");

        //点亮屏幕
        wl.acquire(1000);

        //得到键盘锁管理器对象
        KeyguardManager km = (KeyguardManager) getSystemService(Context.KEYGUARD_SERVICE);
        kl = km.newKeyguardLock("unLock");

        //解锁
        kl.disableKeyguard();
    }

    /**
     * 收尾工作
     */
    private void releese() {
        if (kl != null) {
            //..
            kl.reenableKeyguard();
        }

        rpNode = null;
    }


    /**
     * 当系统连接上你的服务时被调用
     */
    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
    }

    /**
     * 必须重写的方法：系统要中断此service返回的响应时会调用。在整个生命周期会被调用多次。
     */
    @Override
    public void onInterrupt() {
    }

    /**
     * 在系统要关闭此service时调用。
     */
    @Override
    public boolean onUnbind(Intent intent) {
        if (!PackageManagerUtil.isAccessibilitySettingsOn(this)) {
            intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
        }
        return super.onUnbind(intent);
    }

}
