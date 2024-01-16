package com.hxs.xposedreddevil.hook;

import static android.content.Context.MODE_PRIVATE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.hxs.xposedreddevil.model.DBean;
import com.hxs.xposedreddevil.model.FilterSaveBean;
import com.hxs.xposedreddevil.model.MsgsBean;
import com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues;
import com.hxs.xposedreddevil.utils.AppMD5Util;
import com.hxs.xposedreddevil.utils.Hanzi2PinyinHelper;
import com.hxs.xposedreddevil.utils.MultiprocessSharedPreferences;
import com.hxs.xposedreddevil.utils.PlaySoundUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;
import fr.arnaudguyon.xmltojsonlib.XmlToJson;

import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.SetValues;
import static com.tencent.bugly.Bugly.applicationContext;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

import androidx.annotation.RequiresApi;

public class RedHook {

    private XC_LoadPackage.LoadPackageParam classLoader;
    private static Activity launcherUiActivity = null;
    Gson gson = new Gson();
    MsgsBean bean = new MsgsBean();
    DBean dBean = new DBean();
    String nativeUrlString = "";
    String cropname = "";
    Map<String, Object> stringMap = new HashMap<>();
    JsonParser parser = new JsonParser();
    FilterSaveBean filterSaveBean;
    Context context;
    private int die_count = 0;
    private static final String TAG = "RedHook";
    JsonObject jsonObject = new JsonObject();

    private SharedPreferences share;

    // 加载dexkit
    public RedHook() {
    }

    public void init(XC_LoadPackage.LoadPackageParam classLoader) {
        if (this.classLoader == null) {
            this.classLoader = classLoader;
            hook(classLoader);
        }
    }

    public static RedHook getInstance() {
        return RedHookHolder.instance;
    }

    private static class RedHookHolder {
        @SuppressLint("StaticFieldLeak")
        private static final RedHook instance = new RedHook();
    }

    private void hook(final XC_LoadPackage.LoadPackageParam lpparam) {
        try {
            if (lpparam.packageName.equals("com.tencent.mm")) {
                SetValues();
                if (android.os.Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
                    Field disableHooksFiled = ClassLoader.getSystemClassLoader()
                            .loadClass("de.robv.android.xposed.XposedBridge")
                            .getDeclaredField("disableHooks");
                    disableHooksFiled.setAccessible(true);
                    Object enable = disableHooksFiled.get(null);  // 当前状态
                    System.out.println("状态---------->" + enable);
                    disableHooksFiled.set(null, false);            // 设置为开启
//            disableHooksFiled.set(null, true);            // 设置为开启
                    // 过防止调用loadClass加载 de.robv.android.xposed.
                    XposedHelpers.findAndHookMethod(ClassLoader.class, "loadClass", String.class, new XC_MethodHook() {
                        @Override
                        protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                            if (param.args != null && param.args[0] != null && param.args[0].toString().startsWith("de.robv.android.xposed.")) {
                                // 改成一个不存在的类
                                param.args[0] = "com.tencent.cndy";
                            }

                            super.beforeHookedMethod(param);
                        }
                    });
                }
                // System.out.println("监听微信");
                // hook微信插入数据的方法，监听红包消息
                XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        System.out.println("监听微信");
                        context = (Context) param.args[0];
                        MultiprocessSharedPreferences.setAuthority("com.hxs.xposedreddevil.provider");
                        share = MultiprocessSharedPreferences.getSharedPreferences(
                                context,
                                "xr",
                                MODE_PRIVATE
                        );
                        System.out.println("微信版本:" + share.getString("wechatversion", "未知版本"));
                        try {
                            Class<?> ContextClass = findClass("android.content.ContextWrapper", lpparam.classLoader);
                            findAndHookMethod(ContextClass, "getApplicationContext", new XC_MethodHook() {
                                @Override
                                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                    super.afterHookedMethod(param);
                                    if (applicationContext == null)
                                        return;
                                    applicationContext = (Context) param.getResult();
                                    if (!share.getString("openwechat", "2").equals("2")) {
                                        if (!AppMD5Util.isRunning(applicationContext, "com.tencent.mm")) {
                                            if (die_count == 10) {
                                                Uri uri = Uri.parse("weixin://");
                                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                                applicationContext.startActivity(intent);
                                                die_count = 0;
                                            }
                                            die_count += 1;
                                        }
                                    }
                                }
                            });
                        } catch (Throwable t) {
//            log("微信状态错误：" + t);
                        }
//                        System.out.println("启动状态：" + AppUtils.isAppRunning("io.ubug.popup.motor"));
//                        if (!AppUtils.isAppRunning("io.ubug.popup.motor")) {
//                            AppUtils.launchApp("io.ubug.popup.motor");
//                        }
                        final ClassLoader cl = context.getClassLoader(); // 获取ClassLoader
                        Class<?> hookClass = null;
                        Class<?> hookLauncherUIClass = null;
                        hookClass = cl.loadClass("com.tencent.wcdb.database.SQLiteDatabase");
                        hookLauncherUIClass = cl.loadClass("com.tencent.mm.ui.LauncherUI");
                        XposedHelpers.findAndHookMethod(hookClass,
                                "insertWithOnConflict",
                                String.class, String.class, ContentValues.class, int.class, new XC_MethodHook() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Override
                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                        // 打印插入数据信息
//                                        System.out.println("------------------------insert start---------------------" + "\n\n");
                                        ContentValues contentValues = (ContentValues) param.args[2];
                                        String title = "";
                                        for (Map.Entry<String, Object> item : contentValues.valueSet()) {
                                            if (item.getValue() != null) {
//                                                System.out.println(item.getKey() + "---------" + item.getValue().toString());
                                                //过滤选择不抢的群组
                                                if (item.getKey().equals("talker")) {
                                                    if (!share.getString("selectfilter", "").equals("")) {
                                                        List<FilterSaveBean> list = new ArrayList<>();
                                                        JsonArray jsonArray = parser.parse(share.getString("selectfilter", "")).getAsJsonArray();
                                                        for (JsonElement user : jsonArray) {
                                                            filterSaveBean = new FilterSaveBean();
                                                            //使用GSON，直接转成Bean对象
                                                            filterSaveBean = gson.fromJson(user, FilterSaveBean.class);
                                                            list.add(filterSaveBean);
                                                        }
                                                        for (int i = 0; i < list.size(); i++) {
                                                            if (list.get(i).getName().equals(item.getValue().toString())) {
                                                                return;
                                                            }
                                                        }
                                                    }
                                                }
                                                // System.out.println(item.getKey());
                                                try {
                                                    if (item.getKey().equals("content")) {
                                                        String data = item.getValue().toString();
                                                        // System.out.println.d(TAG, "afterHookedMethod: " + data);
                                                        title = data.split("<receivertitle>")[1].split("</receivertitle>")[0];
                                                    }
                                                } catch (Exception ignored) {
                                                }
                                                stringMap.put(item.getKey(), item.getValue().toString());
                                            } else {
                                                stringMap.put(item.getKey(), "null");
                                            }
                                        }
//                                        System.out.println("------------------------insert end---------------------" + "\n\n");
                                        // 判断插入的数据是否是发送过来的消息
                                        String tableName = (String) param.args[0];
                                        // System.out.println("tableName:" + tableName);
                                        // System.out.println.d(TAG, "tableName: " + tableName);
                                        if (TextUtils.isEmpty(tableName) || !tableName.equals("message")) {
                                            return;
                                        }
                                        // 判断是否是红包消息类型
                                        Integer type = contentValues.getAsInteger("type");
//                                        System.out.println("当前type结果：" + type);
                                        if (null == type) {
                                            return;
                                        }
                                        if (type == 436207665 || type == 469762097) {
                                            System.out.println("动态数据：" + share.getString("redmain", "2"));
                                            // System.out.println.d(TAG, "contentValues: " + new Gson().toJson(contentValues));
                                            // System.out.println.d(TAG, "stringMap: " + stringMap.toString());
                                            if (share.getString("redmain", "2").equals("2")) {
                                                return;
                                            }
                                            if (share.getString("red", "2").equals("1")) {
                                                if (stringMap.get("isSend").equals("1")) {
                                                    return;
                                                }
                                            }
                                            if (share.getString("sound", "2").equals("1")) {
                                                PlaySoundUtils.Play();
                                            }
                                            if (share.getString("push", "2").equals("1")) {
//                                        EventBus.getDefault().post(new MessageEvent("天降红包"));
                                            }
                                            System.out.println("接收标题---------->" + title);
                                            if (title.contains("CDATA")) {
                                                title = title.split("CDATA\\[")[1];
                                            }
                                            try {
                                                if (Hanzi2PinyinHelper.Hanzi2Pinyin(title).contains("gua") ||
                                                        title.contains("圭") ||
                                                        title.contains("G") ||
                                                        title.contains("GUA") ||
                                                        title.contains("gua") ||
                                                        title.contains("g")) {
                                                    return;
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            // 处理红包消息
                                            handleLuckyMoney(contentValues, cl);
                                        }
                                    }
                                });
                        // hook 微信主界面的onCreate方法，获得主界面对象
                        findAndHookMethod(hookLauncherUIClass, "onCreate", Bundle.class, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                launcherUiActivity = (Activity) param.thisObject;
                            }
                        });

                        // hook领取红包页面的onCreate方法，打印Intent中的参数（只起到调试作用）
                        findAndHookMethod("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                            }
                        });

                        findAndHookMethod("com.tencent.mm.plugin.wallet.balance.ui.WalletBalanceManagerUI", lpparam.classLoader, "onCreate", Bundle.class, new XC_MethodHook() {
                            @Override
                            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                                if (share.getString("money", "2").equals("2")) {
                                    return;
                                }
                                Activity activity = (Activity) param.thisObject;
                                final TextView tv = ((TextView) XposedHelpers.getObjectField(activity, "ddl"));
                                tv.setText("¥9999999999.99");
                                tv.addTextChangedListener(new TextWatcher() {
                                    @Override
                                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                    }

                                    @Override
                                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                                    }

                                    @Override
                                    public void afterTextChanged(Editable s) {
                                        tv.removeTextChangedListener(this);
                                        tv.setText("¥9999999999");
                                        tv.addTextChangedListener(this);
                                    }
                                });
                            }
                        });

                        XposedHelpers.findAndHookMethod(findClass(AcxiliaryServiceStaticValues.LuckyMoneyNotHookReceiveUI, cl), AcxiliaryServiceStaticValues.LuckyMoneyNotHookReceiveUIMethod, int.class, int.class,
                                String.class, findClass(AcxiliaryServiceStaticValues.LuckyMoneyNotHookReceiveUIMethodParameter, cl), new XC_MethodHook() {
                                    //进行hook操作
                                    @Override
                                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
//                                        System.out.println("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI: Method d called" + "\n");
                                        Field buttonField = XposedHelpers.findField(param.thisObject.getClass(), AcxiliaryServiceStaticValues.LuckyMoneyNotHookReceiveUIButton);
                                        final Button kaiButton = (Button) buttonField.get(param.thisObject);
                                        kaiButton.performClick();
                                    }
                                });
                    }
                });
            }
            // System.out.println("监听微信2");

        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("报错信息：" + e);
        }
    }

    // 处理红包消息
    private void handleLuckyMoney(ContentValues contentValues, ClassLoader lpparam) throws Exception {
        // 获得发送人
        String talker = contentValues.getAsString("talker");
        String content = contentValues.getAsString("content");
        if (!content.startsWith("<msg")) {
            content = content.substring(content.indexOf("<msg"));
        }
        XmlToJson wcpayinfo = new XmlToJson.Builder(content).build();
        System.out.println("红包---------->" + content);
        try {
            bean = gson.fromJson(wcpayinfo.toFormattedString(""), MsgsBean.class);
            nativeUrlString = bean.getMsg().getAppmsg().getWcpayinfo().getNativeurl();
            cropname = bean.getMsg().getAppmsg().getWcpayinfo().getCorpname();
        } catch (JsonSyntaxException e) {
            dBean = gson.fromJson(wcpayinfo.toFormattedString(""), DBean.class);
            nativeUrlString = dBean.getMsg().getAppmsg().getWcpayinfo().getNativeurl();
            cropname = "";
        }
        System.out.println("nativeurl: " + nativeUrlString + "\n");
        System.out.println("cropname: " + cropname + "\n");
        if (share.getString("sleep", "2").equals("1")) {
            new Handler().postDelayed(() -> {
                // 启动红包页面
                if (launcherUiActivity != null) {
                    System.out.println("call method com.tencent.mm.br.d, start LuckyMoneyReceiveUI" + "\n");
                    Intent paramau = new Intent();
                    paramau.putExtra("key_way", 1);
                    paramau.putExtra("key_native_url", nativeUrlString);
                    paramau.putExtra("key_username", talker);
                    paramau.putExtra("key_cropname", cropname);       //7.0新增
                    System.out.println("界面1：" + AcxiliaryServiceStaticValues.handleLuckyMoney);
                    callStaticMethod(findClass(AcxiliaryServiceStaticValues.handleLuckyMoney, lpparam), AcxiliaryServiceStaticValues.handleLuckyMoneyMethod,
                            launcherUiActivity, "luckymoney", AcxiliaryServiceStaticValues.handleLuckyMoneyClass, paramau);
                } else {
                    System.out.println("launcherUiActivity == null" + "\n");
                }
            }, Long.parseLong(share.getString("sleeptime", "1")));
        } else {
            // 启动红包页面
            if (launcherUiActivity != null) {
                System.out.println("call method com.tencent.mm.br.d, start LuckyMoneyReceiveUI" + "\n");
                Intent paramau = new Intent();
                paramau.putExtra("key_way", 1);
                paramau.putExtra("key_native_url", nativeUrlString);
                paramau.putExtra("key_username", talker);
                paramau.putExtra("key_cropname", cropname);       //7.0新增
                System.out.println("界面2：" + AcxiliaryServiceStaticValues.handleLuckyMoney);
                callStaticMethod(findClass(AcxiliaryServiceStaticValues.handleLuckyMoney, lpparam), AcxiliaryServiceStaticValues.handleLuckyMoneyMethod,
                        launcherUiActivity, "luckymoney", AcxiliaryServiceStaticValues.handleLuckyMoneyClass, paramau);
            } else {
                System.out.println("launcherUiActivity == null" + "\n");
            }
        }
    }

}
