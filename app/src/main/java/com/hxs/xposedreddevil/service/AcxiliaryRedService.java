package com.hxs.xposedreddevil.service;

import android.accessibilityservice.AccessibilityService;
import android.annotation.SuppressLint;
import android.app.KeyguardManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Handler;
import android.os.PowerManager;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Display;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;

import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.greendao.DbCarryList;
import com.hxs.xposedreddevil.greendao.DbCarryListDao;
import com.hxs.xposedreddevil.greendao.db.DbManager;
import com.hxs.xposedreddevil.utils.AccessibilityUtils;
import com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues;
import com.hxs.xposedreddevil.utils.DateUtils;
import com.hxs.xposedreddevil.utils.MessageEvent;
import com.hxs.xposedreddevil.utils.PackageManagerUtil;
import com.hxs.xposedreddevil.utils.PinYinUtils;
import com.hxs.xposedreddevil.utils.SQLiteUtils;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.CARRYUI;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.LAUCHER;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.LUCKEY_MONEY_DETAIL;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.LUCKEY_MONEY_RECEIVER;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.OPEN_ID;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.carrypagebtn;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.carrypagenum;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.carrypagetime;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.carrystates;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.chatid;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.chatnameid;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.chatonenameid;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.chatredid;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.msgisredid;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.msgname;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.msgredcontent;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.msgredid;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.redcircle;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.redpagenum;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.redunmsgcircle;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.strredstatus;
import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;
import static com.hxs.xposedreddevil.utils.Constant.RED_LIST;

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

    private int postnum = 0;

    private String username = "";

    private DbCarryList carryList;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onAccessibilityEvent(final AccessibilityEvent event) {
        AcxiliaryServiceStaticValues.SetValues();
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
                    if (PropertiesUtils.getValue(RED_FILE, "nottootkeyword", "2").equals("1")) {
                        if (content.contains("@" + PropertiesUtils.getValue(RED_FILE, "nottootname", ""))) {
                            EventBus.getDefault().post(new MessageEvent(content));
                        }
                    }
                    int i = text.toString().indexOf("[微信红包]");
                    //如果不是微信红包，则不需要做其他工作了
                    if (i == -1)
                        break;
                    int a = text.toString().indexOf("[转账]");
                    //如果不是微信红包，则不需要做其他工作了
                    if (a == -1)
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
                if (LAUCHER.equals(className)) {
                    //开始找红包
                    WindowRed(getRootInActiveWindow());
                }

                //判断是否是显示‘开’的那个红包界面
                if (LUCKEY_MONEY_RECEIVER.equals(className)) {
                    AccessibilityNodeInfo rootNode = getRootInActiveWindow();
                    //开始抢红包
                    findOpenBtn(rootNode);
                }

                //判断是否是转账那个界面
                if (CARRYUI.equals(className)) {
                    AccessibilityNodeInfo rootNode = getRootInActiveWindow();
//                    System.out.println(rootNode.findAccessibilityNodeInfosByViewId(carrypagetime).get(0).getText().toString());
                    //开始抢红包
                    findCarryOpenBtn(rootNode);
                }

                //判断是否是红包领取后的详情界面
                if (isOpenDetail && LUCKEY_MONEY_DETAIL.equals(className)) {
                    try {
                        carryList = new DbCarryList();
                        carryList.setMoney("￥" + getRootInActiveWindow().findAccessibilityNodeInfosByViewId(redpagenum).get(0).getText().toString());
                        carryList.setName(username);
                        carryList.setTime(DateUtils.getYear() + "-" + DateUtils.getMonth() + "-" + DateUtils.getDay() + " " + DateUtils.getHour() + ":" + DateUtils.getMinute());
                        carryList.setStatus("红包");
                        SQLiteUtils.getInstance().addContacts(carryList);
                        isOpenDetail = false;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    //返回桌面
//                    back2Home();
                }
                break;
            case AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED:
                postnum += 1;
                if (event.getPackageName().equals("com.tencent.mm")) {
                    try {
                        List<AccessibilityNodeInfo> itemNodes = getRootInActiveWindow().findAccessibilityNodeInfosByViewId(chatid);
                        if (itemNodes != null && itemNodes.size() != 0) {
                            for (int i = 0; i < itemNodes.size(); i++) {
                                AccessibilityNodeInfo chatContentNode = itemNodes.get(i).findAccessibilityNodeInfosByViewId(chatredid).get(0);
                                if (chatContentNode != null && chatContentNode.getText() != null) {
                                    if (PropertiesUtils.getValue(RED_FILE, "nottootkeyword", "2").equals("1")) {
                                        if (chatContentNode.getText().toString().contains("有人@我")) {
                                            if (postnum == itemNodes.size()) {
                                                EventBus.getDefault().post(new MessageEvent(itemNodes.get(i).findAccessibilityNodeInfosByViewId(chatnameid).
                                                        get(0).getText().toString() + "@了我"));
                                                postnum = 0;
                                            }
                                        }
                                    }
                                    if (chatContentNode.getText().toString().contains("微信红包") || chatContentNode.getText().toString().contains("转账")) {
                                        AccessibilityNodeInfo chatNode;
                                        if (itemNodes.get(i).findAccessibilityNodeInfosByViewId(chatnameid).size() == 0 &&
                                                itemNodes.get(i).findAccessibilityNodeInfosByViewId(chatonenameid).size() == 0) {
                                            continue;
                                        } else {
                                            if (itemNodes.get(i).findAccessibilityNodeInfosByViewId(chatnameid).size() == 0) {
                                                chatNode = itemNodes.get(i).findAccessibilityNodeInfosByViewId(chatonenameid).get(0);
                                            } else {
                                                chatNode = itemNodes.get(i).findAccessibilityNodeInfosByViewId(chatnameid).get(0);
                                            }
                                        }
                                        if (chatNode != null && chatNode.getText() != null) {
                                            if (itemNodes.get(i).
                                                    findAccessibilityNodeInfosByViewId(redcircle).size() > 0) {
                                                AccessibilityNodeInfo circlenode = itemNodes.get(i).
                                                        findAccessibilityNodeInfosByViewId(redcircle).get(0);
                                                if (circlenode != null && circlenode.getText() != null) {
                                                    itemNodes.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                }
                                            } else if (itemNodes.get(i).
                                                    findAccessibilityNodeInfosByViewId(redunmsgcircle).size() > 0) {
                                                AccessibilityNodeInfo circlenode = itemNodes.get(i).
                                                        findAccessibilityNodeInfosByViewId(redunmsgcircle).get(0);
                                                if (PropertiesUtils.getValue(RED_FILE, "nottootdisturb", "2").equals("2")) {
                                                    return;
                                                }
                                                if (circlenode != null) {
                                                    itemNodes.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                                }
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
                        try {
                            WindowRed(getRootInActiveWindow());
                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }
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
        AccessibilityNodeInfo msgnames = null;
        try {
            msgnames = nodeInfo.findAccessibilityNodeInfosByViewId(msgname).get(0);
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
                    if (list.get(i).getParent().findAccessibilityNodeInfosByViewId(msgredcontent).size() > 0) {
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
                            if (list.get(i).findAccessibilityNodeInfosByViewId(msgisredid).size() == 0) {
                                return;
                            }
                            if (list.get(i).isClickable()) {
                                Rect rect = new Rect();
                                list.get(i).getBoundsInScreen(rect);
                                username = msgnames.getText().toString();
                                if (rect.centerX() > (Integer.parseInt(PropertiesUtils.getValue(RED_FILE, "widthPixels", "0")) / 2)) {
                                    String pattern = ".*[(].*\\d[)]";
                                    if (Pattern.compile(pattern).matcher(msgnames.getText().toString()).matches()) {
                                        if (PropertiesUtils.getValue(RED_FILE, "notrooown", "2").equals("1")) {
                                            return;
                                        }
                                    } else {
                                        return;
                                    }
                                }
                                //模拟点击
                                list.get(i).performAction(AccessibilityNodeInfo.ACTION_CLICK);
                                //isOpenRP用于判断该红包是否点击过
                                isOpenRP = true;
                                isOpenDetail = true;
                            }
                        }
                    } else if (list.get(i).getParent().findAccessibilityNodeInfosByViewId(carrystates).size() > 0) {
                        if (list.get(i).getParent().findAccessibilityNodeInfosByViewId(carrystates).get(0)
                                .getText().toString().equals("已被领取")) {
                            return;
                        }
                        if (list.get(i).isClickable()) {
                            Rect rect = new Rect();
                            list.get(i).getBoundsInScreen(rect);
                            username = msgnames.getText().toString();
                            if (rect.centerX() > (Integer.parseInt(PropertiesUtils.getValue(RED_FILE, "widthPixels", "0")) / 2)) {
                                String pattern = ".*[(].*\\d[)]";
                                if (Pattern.compile(pattern).matcher(msgnames.getText().toString()).matches()) {
                                    if (PropertiesUtils.getValue(RED_FILE, "notrooown", "2").equals("1")) {
                                        return;
                                    }
                                } else {
                                    return;
                                }
                            }
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
        } catch (Exception e) {
            e.printStackTrace();
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
                    findOpenBtn(getRootInActiveWindow());
                }
            }, 1500);
        }
    }

    private void findCarryOpenBtn(final AccessibilityNodeInfo rootNode) {

        AccessibilityNodeInfo button_open = AccessibilityUtils.findNodeInfosById(rootNode, carrypagebtn);

        if (button_open != null) {
            final AccessibilityNodeInfo n = button_open;

//            AccessibilityUtils.performClick(n);

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    AccessibilityUtils.performClick(n);
                    if (PropertiesUtils.getValue(RED_FILE, "notrootlist", "2").equals("1")) {
                        carryList = new DbCarryList();
                        carryList.setMoney(rootNode.findAccessibilityNodeInfosByViewId(carrypagenum).get(0).getText().toString());
                        carryList.setName(username);
                        carryList.setTime(DateUtils.getYear() + "-" + DateUtils.getMonth() + "-" + DateUtils.getDay() + " " + DateUtils.getHour() + ":" + DateUtils.getMinute());
                        carryList.setStatus("转账");
                        SQLiteUtils.getInstance().addContacts(carryList);
                    }
                }
            }, 500);

        } else {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    findCarryOpenBtn(getRootInActiveWindow());
                }
            }, 2000);
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
