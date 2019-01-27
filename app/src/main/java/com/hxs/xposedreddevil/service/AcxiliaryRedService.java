package com.hxs.xposedreddevil.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.text.TextUtils;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;

import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.utils.AccessibilityUtils;
import com.hxs.xposedreddevil.utils.PackageManagerUtil;
import com.hxs.xposedreddevil.utils.PinYinUtils;

import java.util.List;

import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;

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

    private String strredstatus = "";   //红包状态
    private String chatredid = "";      //聊天列表红包消息ID
    private String chatid = "";         //聊天item ID
    private String redcircle = "";      //消息小红点ID
    private String chatnameid = "";     //聊天对象ID
    private String msgredid = "";       //聊天页面红包ID
    private String msgredcontent = "";  //聊天页面红包内容ID(恭喜发财，大吉大利)
    private String msgisredid = "";     //微信红包下方微信红包四个字

    /**
     * 微信几个页面的包名+地址。用于判断在哪个页面
     */
    private String LAUCHER = "com.tencent.mm.ui.LauncherUI";
    private String LUCKEY_MONEY_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    private String LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    private String OPEN_ID = "com.tencent.mm:id/cv0";

    private String wechatversion = PropertiesUtils.getValue(RED_FILE, "wechatversion", "");

    @Override
    public void onAccessibilityEvent(final AccessibilityEvent event) {
        if (wechatversion.equals("")) {
            LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
            OPEN_ID = "com.tencent.mm:id/cnu";
            strredstatus = "com.tencent.mm:id/alw";
            chatredid = "com.tencent.mm:id/azn";
            chatid = "com.tencent.mm:id/azj";
            redcircle = "com.tencent.mm:id/lu";
            chatnameid = "com.tencent.mm:id/azl";
            msgredid = "com.tencent.mm:id/aku";
            msgredcontent = "com.tencent.mm:id/alv";
            msgisredid = "com.tencent.mm:id/alx";
        } else if (wechatversion.equals("7.0.0")) {
            LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            OPEN_ID = "com.tencent.mm:id/cv0";
            strredstatus = "com.tencent.mm:id/ape";
            chatredid = "com.tencent.mm:id/b4q";
            chatid = "com.tencent.mm:id/b4m";
            redcircle = "com.tencent.mm:id/mm";
            chatnameid = "com.tencent.mm:id/b4o";
            msgredid = "com.tencent.mm:id/ao4";
            msgredcontent = "com.tencent.mm:id/apd";
            msgisredid = "com.tencent.mm:id/apf";
        } else if (wechatversion.equals("6.7.3")) {
            LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
            OPEN_ID = "com.tencent.mm:id/cnu";
            strredstatus = "com.tencent.mm:id/alw";
            chatredid = "com.tencent.mm:id/azn";
            chatid = "com.tencent.mm:id/azj";
            redcircle = "com.tencent.mm:id/lu";
            chatnameid = "com.tencent.mm:id/azl";
            msgredid = "com.tencent.mm:id/aku";
            msgredcontent = "com.tencent.mm:id/alv";
            msgisredid = "com.tencent.mm:id/alx";
        }
        //接收事件
        int eventType = event.getEventType();
        if (PropertiesUtils.getValue(RED_FILE, "rednorootmain", "2").equals("2")) {
            return;
        }
        switch (eventType) {
            case AccessibilityEvent.TYPE_NOTIFICATION_STATE_CHANGED:
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
                        isOpenRP = false;
                    }
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED:
                //监测到窗口变化。
                String className = event.getClassName().toString();

                //判断是否是微信聊天界面
//                if (LAUCHER.equals(className)) {
//                    //获取当前聊天页面的根布局
//                    AccessibilityNodeInfo rootNode = getRootInActiveWindow();
//                    //开始找红包
//                    findStuff(rootNode);
//                }

                //判断是否是显示‘开’的那个红包界面
                if (LUCKEY_MONEY_RECEIVER.equals(className)) {
                    AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                    //开始抢红包
                    findOpenBtn(rootNode);
                }

                //判断是否是红包领取后的详情界面
                if (isOpenDetail && LUCKEY_MONEY_DETAIL.equals(className)) {

                    isOpenDetail = false;
                    //返回桌面
                    back2Home();
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                if (event.getPackageName().equals("com.tencent.mm")) {
                    try {
                        List<AccessibilityNodeInfo> itemNodes = getRootInActiveWindow().findAccessibilityNodeInfosByViewId(chatid);
                        if (itemNodes != null && itemNodes.size() != 0) {
                            for (int i = 0; i < itemNodes.size(); i++) {
                                AccessibilityNodeInfo chatContentNode = itemNodes.get(i).findAccessibilityNodeInfosByViewId(chatredid).get(0);
                                if (chatContentNode != null && chatContentNode.getText() != null) {
                                    if (chatContentNode.getText().toString().contains("微信红包")) {
                                        AccessibilityNodeInfo chatNode;
                                        if (itemNodes.get(i).findAccessibilityNodeInfosByViewId(chatnameid).size() == 0) {
                                            continue;
                                        } else {
                                            chatNode = itemNodes.get(i).findAccessibilityNodeInfosByViewId(chatnameid).get(0);
                                        }
                                        if (chatNode != null && chatNode.getText() != null) {
                                            AccessibilityNodeInfo circlenode = itemNodes.get(i).
                                                    findAccessibilityNodeInfosByViewId(redcircle).get(0);
                                            if (circlenode != null && circlenode.getText() != null) {
                                                itemNodes.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                            }
                                        }
                                    } else if (chatContentNode.getText().toString().isEmpty()) {
                                        continue;
                                    }
                                }
                            }
                        } else {
                            WindowRed(getRootInActiveWindow());
                        }
                    } catch (Exception e) {
                        WindowRed(getRootInActiveWindow());
                    }
                }
                break;
        }
        //释放一下资源。
        releese();
    }

    private void WindowRed(AccessibilityNodeInfo nodeInfo) {
        if (nodeInfo == null) {
            return;
        }
        // 找到领取红包的点击事件
        List<AccessibilityNodeInfo> list = null;
        list = nodeInfo.findAccessibilityNodeInfosByViewId(msgredid);
        // 最新的红包领起
        for (int i = list.size() - 1; i >= 0; i--) {
            // 通过调试可知[领取红包]是text，本身不可被点击，用getParent()获取可被点击的对象
            if (list.get(i) == null) {
                continue;
            }
            AccessibilityNodeInfo parent = list.get(i).getParent();
            // 谷歌重写了toString()方法，不能用它获取ClassName@hashCode串
            while (parent != null) {
                String redcontent = "";     //红包内容
                try {
                    redcontent = list.get(i).getParent().findAccessibilityNodeInfosByViewId(msgredcontent).get(0).getText().toString();
                    if (PinYinUtils.getPingYin(redcontent).contains("gua") ||
                            redcontent.contains("圭") ||
                            redcontent.contains("G") ||
                            redcontent.contains("GUA") ||
                            redcontent.contains("gua") ||
                            redcontent.contains("g")) {
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                AccessibilityNodeInfo redstatus = null;
                try {
                    if (list.get(i).getParent().findAccessibilityNodeInfosByViewId(strredstatus) == null
                            || list.get(i).getParent().findAccessibilityNodeInfosByViewId(strredstatus).size() == 0) {
                        redstatus = null;
                    } else {
                        redstatus = list.get(i).getParent().findAccessibilityNodeInfosByViewId(strredstatus).get(0);
                    }
                } catch (Exception e) {
                    redstatus = null;
                }
                if (redstatus == null) {
                    if(list.get(i).findAccessibilityNodeInfosByViewId(msgisredid).size()==0){
                        return;
                    }
                    if (list.get(i).isClickable()) {
                        //模拟点击
                        list.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                        //isOpenRP用于判断该红包是否点击过
                        isOpenRP = true;
                    }
                }
                break; // 只领最新的一个红包
            }
            //判断是否已经打开过那个最新的红包了，是的话就跳出for循环，不是的话继续遍历
            if (isOpenRP) {
                return;
            } else {
                WindowRed(nodeInfo);
            }
        }
    }

    //
    private void findOpenBtn(AccessibilityNodeInfo rootNode) {
        AccessibilityNodeInfo button_open = AccessibilityUtils.findNodeInfosById(rootNode, OPEN_ID);
        if (button_open != null) {
            final AccessibilityNodeInfo n = button_open;

            AccessibilityUtils.performClick(n);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AccessibilityUtils.performClick(n);
                }
            }, 1500);

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                }
            }, 1500);
        }
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
     * 返回桌面
     */
    private void back2Home() {
        Intent home = new Intent(Intent.ACTION_MAIN);
        home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        home.addCategory(Intent.CATEGORY_HOME);
        startActivity(home);
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
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        }
        return super.onUnbind(intent);
    }

}
