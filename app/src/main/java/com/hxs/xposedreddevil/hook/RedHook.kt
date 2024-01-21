package com.hxs.xposedreddevil.hook

import android.annotation.SuppressLint
import android.app.Activity
import android.app.ActivityManager
import android.app.Application
import android.app.usage.UsageEvents
import android.app.usage.UsageStatsManager
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.widget.Button
import android.widget.TextView
import androidx.annotation.RequiresApi
import cn.xiaowine.dsp.DSP
import cn.xiaowine.dsp.data.MODE
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.google.gson.JsonSyntaxException
import com.hxs.xposedreddevil.BuildConfig
import com.hxs.xposedreddevil.config.SafeConfig
import com.hxs.xposedreddevil.model.DBean
import com.hxs.xposedreddevil.model.FilterSaveBean
import com.hxs.xposedreddevil.model.MsgsBean
import com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues
import com.hxs.xposedreddevil.utils.Hanzi2PinyinHelper
import com.hxs.xposedreddevil.utils.PlaySoundUtils
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import fr.arnaudguyon.xmltojsonlib.XmlToJson
import java.lang.reflect.Field

class RedHook  // 加载dexkit
{
    private var classLoader: XC_LoadPackage.LoadPackageParam? = null
    var gson: Gson = Gson()
    var bean: MsgsBean = MsgsBean()
    var dBean: DBean = DBean()
    var nativeUrlString = ""
    var cropname = ""
    var stringMap: MutableMap<String, Any> = HashMap()
    var parser: JsonParser = JsonParser()
    var filterSaveBean: FilterSaveBean? = null
    lateinit var context: Context
    private var die_count = 0
    var jsonObject: JsonObject = JsonObject()
    private val activity = "" //当前页面
    private var content = "" //返回的内容
    lateinit var config: SafeConfig
    fun init(classLoader: XC_LoadPackage.LoadPackageParam) {
        if (this.classLoader == null) {
            this.classLoader = classLoader
            hook(classLoader)
        }
    }

    public object RedHookHolder {
        @SuppressLint("StaticFieldLeak")
        var instance = RedHook()
    }

    @SuppressLint("PrivateApi")
    private fun hook(lpparam: XC_LoadPackage.LoadPackageParam) {
        try {
            if (lpparam.packageName == "com.tencent.mm") {
                AcxiliaryServiceStaticValues.SetValues()
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O_MR1) {
                    val disableHooksFiled = ClassLoader.getSystemClassLoader()
                        .loadClass("de.robv.android.xposed.XposedBridge")
                        .getDeclaredField("disableHooks")
                    disableHooksFiled.isAccessible = true
                    val enable = disableHooksFiled[null] // 当前状态
                    println("状态---------->$enable")
                    disableHooksFiled[null] = false // 设置为开启
                    //            disableHooksFiled.set(null, true);            // 设置为开启
                    // 过防止调用loadClass加载 de.robv.android.xposed.
                    XposedHelpers.findAndHookMethod(
                        ClassLoader::class.java,
                        "loadClass",
                        String::class.java,
                        object : XC_MethodHook() {
                            @Throws(Throwable::class)
                            protected override fun beforeHookedMethod(param: MethodHookParam) {
                                if (param.args != null && param.args.get(0) != null && param.args[0].toString()
                                        .startsWith("de.robv.android.xposed.")
                                ) {
                                    // 改成一个不存在的类
                                    param.args[0] = "com.tencent.cndy"
                                }
                                super.beforeHookedMethod(param)
                            }
                        })
                }
                // System.out.println("监听微信");
                // hook微信插入数据的方法，监听红包消息
                XposedHelpers.findAndHookMethod(
                    Application::class.java,
                    "attach",
                    Context::class.java,
                    object : XC_MethodHook() {
                        @Throws(Throwable::class)
                        protected override fun afterHookedMethod(param: MethodHookParam) {
                            println("监听微信")
                            println(
                                "可否操作：" + DSP.init(
                                    param.thisObject as Application,
                                    BuildConfig.APPLICATION_ID,
                                    MODE.HOOK,
                                    true
                                )
                            )
                            config = SafeConfig()
                            context = param.args[0] as Context
                            println("微信版本:" + config.wechatversion)
                            try {
                                val ContextClass: Class<*> = XposedHelpers.findClass(
                                    "android.content.ContextWrapper",
                                    lpparam.classLoader
                                )
                                XposedHelpers.findAndHookMethod(
                                    ContextClass,
                                    "getApplicationContext",
                                    object : XC_MethodHook() {
                                        @Throws(Throwable::class)
                                        protected override fun afterHookedMethod(param: MethodHookParam) {
                                            super.afterHookedMethod(param)
//                                            if (share!!.getString("openwechat", "2") != "2") {
//                                                if (!AppMD5Util.isRunning(
//                                                        Bugly.applicationContext,
//                                                        "com.tencent.mm"
//                                                    )
//                                                ) {
//                                                    if (die_count == 10) {
//                                                        val uri = Uri.parse("weixin://")
//                                                        val intent = Intent(Intent.ACTION_VIEW, uri)
//                                                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//                                                        Bugly.applicationContext.startActivity(
//                                                            intent
//                                                        )
//                                                        die_count = 0
//                                                    }
//                                                    die_count += 1
//                                                }
//                                            }
                                        }
                                    })
                            } catch (t: Throwable) {
//            log("微信状态错误：" + t);
                            }
                            //                        System.out.println("启动状态：" + AppUtils.isAppRunning("io.ubug.popup.motor"));
//                        if (!AppUtils.isAppRunning("io.ubug.popup.motor")) {
//                            AppUtils.launchApp("io.ubug.popup.motor");
//                        }
                            val cl = context!!.classLoader // 获取ClassLoader
                            var hookClass: Class<*>? = null
                            var hookLauncherUIClass: Class<*>? = null
                            hookClass = cl.loadClass("com.tencent.wcdb.database.SQLiteDatabase")
                            hookLauncherUIClass = cl.loadClass("com.tencent.mm.ui.LauncherUI")
                            XposedHelpers.findAndHookMethod(hookClass,
                                "insertWithOnConflict",
                                String::class.java,
                                String::class.java,
                                ContentValues::class.java,
                                Int::class.javaPrimitiveType,
                                object : XC_MethodHook() {
                                    @RequiresApi(api = Build.VERSION_CODES.O)
                                    @Throws(
                                        Throwable::class
                                    )
                                    protected override fun afterHookedMethod(param: MethodHookParam) {
                                        // 打印插入数据信息
//                                        System.out.println("------------------------insert start---------------------" + "\n\n");
                                        val contentValues = param.args.get(2) as ContentValues
                                        var title = ""
                                        for ((key, value) in contentValues.valueSet()) {
                                            if (value != null) {
                                                if (key.contains("content")) {
                                                    content = value.toString()
                                                }
                                                //                                                System.out.println(item.getKey() + "---------" + item.getValue().toString().split("\n"));
                                                //过滤选择不抢的群组
                                                if (key == "talker") {
                                                    if (config.selectfilter != ""
                                                    ) {
                                                        val list: MutableList<FilterSaveBean?> =
                                                            ArrayList<FilterSaveBean?>()
                                                        val jsonArray: JsonArray = parser.parse(
                                                            config.selectfilter
                                                        ).asJsonArray
                                                        for (user in jsonArray) {
                                                            filterSaveBean = FilterSaveBean()
                                                            //使用GSON，直接转成Bean对象
                                                            filterSaveBean =
                                                                gson.fromJson<FilterSaveBean>(
                                                                    user,
                                                                    FilterSaveBean::class.java
                                                                )
                                                            list.add(filterSaveBean)
                                                        }
                                                        for (i in list.indices) {
                                                            if (list[i]!!.name == value.toString()) {
                                                                return
                                                            }
                                                        }
                                                    }
                                                }
                                                //                                                System.out.println(item.getKey());
                                                try {
                                                    if (key == "content") {
                                                        val data = value.toString()
                                                        // System.out.println.d(TAG, "afterHookedMethod: " + data);
//                                                        System.out.println("头头：" + title);
                                                        title =
                                                            data.split("<receivertitle>".toRegex())
                                                                .dropLastWhile { it.isEmpty() }
                                                                .toTypedArray()[1].split("</receivertitle>".toRegex())
                                                                .dropLastWhile { it.isEmpty() }
                                                                .toTypedArray()[0]
                                                    }
                                                } catch (ignored: Exception) {
                                                }
                                                stringMap[key] = value.toString()
                                            } else {
                                                stringMap[key] = "null"
                                            }
                                        }
                                        //                                        System.out.println("------------------------insert end---------------------" + "\n\n");
                                        // 判断插入的数据是否是发送过来的消息
                                        val tableName = param.args.get(0) as String
                                        // System.out.println("tableName:" + tableName);
                                        // System.out.println.d(TAG, "tableName: " + tableName);
                                        if (TextUtils.isEmpty(tableName) || tableName != "message") {
                                            return
                                        }
                                        // 判断是否是红包消息类型
                                        val type = contentValues.getAsInteger("type") ?: return
                                        //                                        System.out.println("当前type结果：" + type);
                                        if (type == 436207665 || type == 469762097) {
                                            println("动态数据：" + config.redMain)
                                            //                                            if (!activity.contains("com.tencent.mm.ui.LauncherUI")) {
//                                                Uri uri = Uri.parse("weixin://");
//                                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                                context.startActivity(intent);
                                            if (handleBefore(title)) {
                                                // 处理红包消息
                                                handleLuckyMoney(contentValues, cl)
                                            }
                                            //                                            }
                                        }
                                    }
                                })
                            // hook 微信主界面的onCreate方法，获得主界面对象
                            XposedHelpers.findAndHookMethod(
                                hookLauncherUIClass,
                                "onCreate",
                                Bundle::class.java,
                                object : XC_MethodHook() {
                                    @Throws(Throwable::class)
                                    protected override fun afterHookedMethod(param: MethodHookParam) {
                                        launcherUiActivity = param.thisObject as Activity?
                                    }
                                })

                            // hook领取红包页面的onCreate方法，打印Intent中的参数（只起到调试作用）
                            XposedHelpers.findAndHookMethod(
                                "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI",
                                lpparam.classLoader,
                                "onCreate",
                                Bundle::class.java,
                                object : XC_MethodHook() {
                                    @Throws(Throwable::class)
                                    protected override fun afterHookedMethod(param: MethodHookParam) {
                                    }
                                })
                            XposedHelpers.findAndHookMethod(
                                "com.tencent.mm.plugin.wallet.balance.ui.WalletBalanceManagerUI",
                                lpparam.classLoader,
                                "onCreate",
                                Bundle::class.java,
                                object : XC_MethodHook() {
                                    @Throws(Throwable::class)
                                    protected override fun afterHookedMethod(param: MethodHookParam) {
                                        if (!config.money) {
                                            return
                                        }
                                        val activity = param.thisObject as Activity
                                        val tv = XposedHelpers.getObjectField(
                                            activity,
                                            "ddl"
                                        ) as TextView
                                        tv.text = "¥9999999999.99"
                                        tv.addTextChangedListener(object : TextWatcher {
                                            override fun beforeTextChanged(
                                                s: CharSequence,
                                                start: Int,
                                                count: Int,
                                                after: Int
                                            ) {
                                            }

                                            override fun onTextChanged(
                                                s: CharSequence,
                                                start: Int,
                                                before: Int,
                                                count: Int
                                            ) {
                                            }

                                            override fun afterTextChanged(s: Editable) {
                                                tv.removeTextChangedListener(this)
                                                tv.text = "¥9999999999"
                                                tv.addTextChangedListener(this)
                                            }
                                        })
                                    }
                                })
                            XposedHelpers.findAndHookMethod(XposedHelpers.findClass(
                                AcxiliaryServiceStaticValues.LuckyMoneyNotHookReceiveUI,
                                cl
                            ),
                                AcxiliaryServiceStaticValues.LuckyMoneyNotHookReceiveUIMethod,
                                Int::class.javaPrimitiveType,
                                Int::class.javaPrimitiveType,
                                String::class.java,
                                XposedHelpers.findClass(
                                    AcxiliaryServiceStaticValues.LuckyMoneyNotHookReceiveUIMethodParameter,
                                    cl
                                ),
                                object : XC_MethodHook() {
                                    //进行hook操作
                                    @Throws(Throwable::class)
                                    protected override fun afterHookedMethod(param: MethodHookParam) {
//                                        System.out.println("com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI: Method d called" + "\n");
                                        val buttonField: Field = XposedHelpers.findField(
                                            param.thisObject.javaClass,
                                            AcxiliaryServiceStaticValues.LuckyMoneyNotHookReceiveUIButton
                                        )
                                        val kaiButton = buttonField[param.thisObject] as Button
                                        kaiButton.performClick()
                                    }
                                })
                        }
                    })
            }
            // System.out.println("监听微信2");
        } catch (e: Exception) {
            e.printStackTrace()
            println("报错信息：$e")
        }
    }

    private fun handleBefore(title: String): Boolean {
        println("safe状态：" + config.redMain)
        var title = title
        if (!config.redMain) {
            return false
        }
        if (config.red) {
            if (stringMap["isSend"] == "1") {
                return false
            }
        }
        if (config.sound) {
            PlaySoundUtils.Play()
        }
        if (config.push) {
//                                        EventBus.getDefault().post(new MessageEvent("天降红包"));
        }
        println("接收标题---------->$title")
        if (title.contains("CDATA")) {
            title =
                title.split("CDATA\\[".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()[1]
        }
        try {
            if (Hanzi2PinyinHelper.Hanzi2Pinyin(title).contains("gua") ||
                title.contains("圭") ||
                title.contains("G") ||
                title.contains("GUA") ||
                title.contains("gua") ||
                title.contains("g")
            ) {
                return false
            }
        } catch (e: Exception) {
            e.printStackTrace()
            println(e)
        }
        return true
    }

    // 处理红包消息
    @Throws(Exception::class)
    private fun handleLuckyMoney(contentValues: ContentValues, lpparam: ClassLoader) {
        // 获得发送人
        val talker = contentValues.getAsString("talker")
        var content = contentValues.getAsString("content")
        if (!content.startsWith("<msg")) {
            content = content.substring(content.indexOf("<msg"))
        }
        val wcpayinfo: XmlToJson = XmlToJson.Builder(content).build()
        println("红包---------->$content")
        try {
            bean = gson.fromJson<MsgsBean>(wcpayinfo.toFormattedString(""), MsgsBean::class.java)
            nativeUrlString = bean.msg.appmsg.wcpayinfo.nativeurl
            cropname = bean.msg.appmsg.wcpayinfo.corpname
        } catch (e: JsonSyntaxException) {
            dBean = gson.fromJson<DBean>(wcpayinfo.toFormattedString(""), DBean::class.java)
            nativeUrlString = dBean.msg.appmsg.wcpayinfo.nativeurl
            cropname = ""
        }
        println("nativeurl: $nativeUrlString\n")
        println("cropname: $cropname\n")
        if (config.sleep) {
            Handler().postDelayed({
                // 启动红包页面
                if (launcherUiActivity != null) {
                    println("call method com.tencent.mm.br.d, start LuckyMoneyReceiveUI" + "\n")
                    val paramau = Intent()
                    paramau.putExtra("key_way", 1)
                    paramau.putExtra("key_native_url", nativeUrlString)
                    paramau.putExtra("key_username", talker)
                    paramau.putExtra("key_cropname", cropname) //7.0新增
                    println("界面1：" + AcxiliaryServiceStaticValues.handleLuckyMoney)
                    XposedHelpers.callStaticMethod(
                        XposedHelpers.findClass(
                            AcxiliaryServiceStaticValues.handleLuckyMoney,
                            lpparam
                        ),
                        AcxiliaryServiceStaticValues.handleLuckyMoneyMethod,
                        launcherUiActivity,
                        "luckymoney",
                        AcxiliaryServiceStaticValues.handleLuckyMoneyClass,
                        paramau
                    )
                } else {
                    println("launcherUiActivity == null" + "\n")
                }
            }, config.sleeptime.toLong())
        } else {
            // 启动红包页面
            if (launcherUiActivity != null) {
                println("call method com.tencent.mm.br.d, start LuckyMoneyReceiveUI" + "\n")
                val paramau = Intent()
                paramau.putExtra("key_way", 1)
                paramau.putExtra("key_native_url", nativeUrlString)
                paramau.putExtra("key_username", talker)
                paramau.putExtra("key_cropname", cropname) //7.0新增
                println("界面2：" + AcxiliaryServiceStaticValues.handleLuckyMoney)
                XposedHelpers.callStaticMethod(
                    XposedHelpers.findClass(AcxiliaryServiceStaticValues.handleLuckyMoney, lpparam),
                    AcxiliaryServiceStaticValues.handleLuckyMoneyMethod,
                    launcherUiActivity,
                    "luckymoney",
                    AcxiliaryServiceStaticValues.handleLuckyMoneyClass,
                    paramau
                )
            } else {
                println("launcherUiActivity == null" + "\n")
            }
        }
    }

    companion object {
        private var launcherUiActivity: Activity? = null
        private const val TAG = "RedHook"

        @JvmStatic
        val instance: RedHook
            get() = RedHookHolder.instance
    }
}
