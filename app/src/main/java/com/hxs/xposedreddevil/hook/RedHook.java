package com.hxs.xposedreddevil.hook;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AndroidAppHelper;
import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.model.DBean;
import com.hxs.xposedreddevil.model.FilterSaveBean;
import com.hxs.xposedreddevil.model.MsgsBean;
import com.hxs.xposedreddevil.model.RedHookBean;
import com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues;
import com.hxs.xposedreddevil.utils.AppMD5Util;
import com.hxs.xposedreddevil.utils.PinYinUtils;
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
import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;
import static com.tencent.bugly.Bugly.applicationContext;
import static de.robv.android.xposed.XposedBridge.log;
import static de.robv.android.xposed.XposedHelpers.callMethod;
import static de.robv.android.xposed.XposedHelpers.callStaticMethod;
import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import static de.robv.android.xposed.XposedHelpers.findClass;

public class RedHook {

    private XC_LoadPackage.LoadPackageParam classLoader;
    private static Activity launcherUiActivity = null;
    Gson gson = new Gson();
    MsgsBean bean = new MsgsBean();
    DBean dBean = new DBean();
    String nativeUrlString = "";
    String cropname = "";
    String data = "";
    Map<String, Object> stringMap = new HashMap<>();
    JsonParser parser = new JsonParser();
    FilterSaveBean filterSaveBean;

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
                    log("状态---------->" + enable);
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
                log("监听微信");
                // hook微信插入数据的方法，监听红包消息
                XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {
                    @Override
                    protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                        log("监听微信");
                        final ClassLoader cl = ((Context) param.args[0]).getClassLoader(); // 获取ClassLoader
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
//                                        log("------------------------insert start---------------------" + "\n\n");
                                        ContentValues contentValues = (ContentValues) param.args[2];
                                        String title = "";
                                        for (Map.Entry<String, Object> item : contentValues.valueSet()) {
                                            if (item.getValue() != null) {
//                                                log(item.getKey() + "---------" + item.getValue().toString());
                                                //过滤选择不抢的群组
                                                if (item.getKey().equals("talker")) {
                                                    if (!PropertiesUtils.getValue(RED_FILE, "selectfilter", "").equals("")) {
                                                        List<FilterSaveBean> list = new ArrayList<>();
                                                        JsonArray jsonArray = parser.parse(PropertiesUtils
                                                                .getValue(RED_FILE, "selectfilter", "")).getAsJsonArray();
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
                                                log(item.getKey());
                                                try {
                                                    if (item.getKey().equals("content")) {
                                                        String data = item.getValue().toString();
                                                        title = data.split("<receivertitle>")[1].split("</receivertitle>")[0];
                                                    }
                                                } catch (Exception e) {
                                                    e.printStackTrace();
                                                }
                                                stringMap.put(item.getKey(), item.getValue().toString());
                                            } else {
                                                stringMap.put(item.getKey(), "null");
                                            }
                                        }
//                                        log("------------------------insert end---------------------" + "\n\n");
                                        // 判断插入的数据是否是发送过来的消息
                                        String tableName = (String) param.args[0];
                                        log("tableName:" + tableName);
                                        if (TextUtils.isEmpty(tableName) || !tableName.equals("message")) {
                                            return;
                                        }
                                        // 判断是否是红包消息类型
                                        Integer type = contentValues.getAsInteger("type");
                                        log("type" + type);
                                        if (null == type) {
                                            return;
                                        }
                                        if (type == 436207665 || type == 469762097) {
                                            if (PropertiesUtils.getValue(RED_FILE, "redmain", "2").equals("2")) {
                                                return;
                                            }
                                            if (PropertiesUtils.getValue(RED_FILE, "red", "2").equals("1")) {
                                                if (stringMap.get("isSend").equals("1")) {
                                                    return;
                                                }
                                            }
                                            if (PropertiesUtils.getValue(RED_FILE, "sound", "2").equals("1")) {
                                                PlaySoundUtils.Play();
                                            }
                                            if (PropertiesUtils.getValue(RED_FILE, "push", "2").equals("1")) {
//                                        EventBus.getDefault().post(new MessageEvent("天降红包"));
                                            }
                                            log("接收标题---------->" + title);
                                            if (title.contains("CDATA")) {
                                                title = title.split("CDATA\\[")[1];
                                            }
                                            if (PinYinUtils.getPingYin(title).contains("gua") ||
                                                    title.contains("圭") ||
                                                    title.contains("G") ||
                                                    title.contains("GUA") ||
                                                    title.contains("gua") ||
                                                    title.contains("g")) {
                                                return;
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
                                if (PropertiesUtils.getValue(RED_FILE, "money", "2").equals("2")) {
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
//                                log("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI: Method d called" + "\n");
                                        Field buttonField = XposedHelpers.findField(param.thisObject.getClass(), AcxiliaryServiceStaticValues.LuckyMoneyNotHookReceiveUIButton);
                                        final Button kaiButton = (Button) buttonField.get(param.thisObject);
                                        kaiButton.performClick();
                                    }
                                });
                    }
                });
            }
            log("监听微信2");

        } catch (Exception e) {
            e.printStackTrace();
            log("报错信息：" + e);
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
        log("红包---------->" + content);
        try {
            bean = gson.fromJson(wcpayinfo.toFormattedString(""), MsgsBean.class);
            nativeUrlString = bean.getMsg().getAppmsg().getWcpayinfo().getNativeurl();
            cropname = bean.getMsg().getAppmsg().getWcpayinfo().getCorpname();
        } catch (JsonSyntaxException e) {
            dBean = gson.fromJson(wcpayinfo.toFormattedString(""), DBean.class);
            nativeUrlString = dBean.getMsg().getAppmsg().getWcpayinfo().getNativeurl();
            cropname = "";
        }
        log("nativeurl: " + nativeUrlString + "\n");
        log("cropname: " + cropname + "\n");
        if (PropertiesUtils.getValue(RED_FILE, "sleep", "2").equals("1")) {
            new Handler().postDelayed(() -> {
                // 启动红包页面
                if (launcherUiActivity != null) {
                    log("call method com.tencent.mm.br.d, start LuckyMoneyReceiveUI" + "\n");
                    Intent paramau = new Intent();
                    paramau.putExtra("key_way", 1);
                    paramau.putExtra("key_native_url", nativeUrlString);
                    paramau.putExtra("key_username", talker);
                    paramau.putExtra("key_cropname", cropname);       //7.0新增
                    System.out.println("界面："+AcxiliaryServiceStaticValues.handleLuckyMoney);
                    callStaticMethod(findClass(AcxiliaryServiceStaticValues.handleLuckyMoney, lpparam), AcxiliaryServiceStaticValues.handleLuckyMoneyMethod,
                            launcherUiActivity, "luckymoney", AcxiliaryServiceStaticValues.handleLuckyMoneyClass, paramau);
                } else {
                    log("launcherUiActivity == null" + "\n");
                }
            }, Long.parseLong(PropertiesUtils.getValue(RED_FILE, "sleeptime", "1")));
        } else {
            // 启动红包页面
            if (launcherUiActivity != null) {
                log("call method com.tencent.mm.br.d, start LuckyMoneyReceiveUI" + "\n");
                Intent paramau = new Intent();
                paramau.putExtra("key_way", 1);
                paramau.putExtra("key_native_url", nativeUrlString);
                paramau.putExtra("key_username", talker);
                paramau.putExtra("key_cropname", cropname);       //7.0新增
                callStaticMethod(findClass(AcxiliaryServiceStaticValues.handleLuckyMoney, lpparam), AcxiliaryServiceStaticValues.handleLuckyMoneyMethod,
                        launcherUiActivity, "luckymoney", AcxiliaryServiceStaticValues.handleLuckyMoneyClass, paramau);
            } else {
                log("launcherUiActivity == null" + "\n");
            }
        }
    }

}
