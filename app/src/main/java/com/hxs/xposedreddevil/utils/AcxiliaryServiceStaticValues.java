package com.hxs.xposedreddevil.utils;

import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;

import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;

public class AcxiliaryServiceStaticValues {

    //免root
    public static String strredstatus = "";   //红包状态(抢完红包后红包item中会出现已领取)
    public static String chatredid = "";      //聊天列表红包消息ID（聊天列表item中的消息）
    public static String chatid = "";         //聊天item ID（聊天列表item）
    public static String redcircle = "";      //消息小红点ID
    public static String redunmsgcircle = ""; //消息小红点ID(设置为免打扰)
    public static String chatnameid = "";     //聊天对象ID（聊天列表群聊名称id）
    public static String chatonenameid = "";  //聊天对象ID（聊天列表一对一名称id）
    public static String msgredid = "";       //聊天页面红包转账ID（聊天页面红包转账id）
    public static String msgredcontent = "";  //聊天页面红包内容ID(恭喜发财，大吉大利)
    public static String msgisredid = "";     //微信红包下方微信红包四个字
    public static String msgname = "";        //微信聊天界面对方昵称
    public static String carrynum = "";       //微信聊天页面转账金额
    public static String carrystates = "";    //微信聊天页面转账状态
    public static String carrypagenum = "";   //微信转账金额
    public static String carrypagetime = "";  //微信转账时间
    public static String carrypagebtn = "";   //微信确认按钮
    public static String redpagenum = "";   //微信红包详情页金额
    public static String OPEN_ID = "com.tencent.mm:id/cv0";   //开按钮
    public static String redclose = "";     //红包详情页面关闭按钮
    public static String carryclose = "";   //转账详情页面关闭按钮
    public static String userhead = "";   //本人头像
    public static String chatitem = "";   //聊天列表item
    //xposed
    public static String LuckyMoneyNotHookReceiveUI = ""; //红包页面名称
    public static String LuckyMoneyNotHookReceiveUIMethod = "";     //红包页面方法
    public static String LuckyMoneyNotHookReceiveUIMethodParameter = "";    //红包页面方法参数
    public static String LuckyMoneyNotHookReceiveUIButton = "";     //“开”按钮
    public static String handleLuckyMoney = "";     //处理红包页面
    public static String handleLuckyMoneyMethod = "";     //处理红包页面方法
    public static String handleLuckyMoneyClass = "";     //处理红包页面方法中的处理红包页面名称


    /**
     * 微信几个页面的包名+地址。用于判断在哪个页面
     */
    public static String LAUCHER = "com.tencent.mm.ui.LauncherUI";
    public static String LUCKEY_MONEY_DETAIL = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyDetailUI";
    public static String LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyReceiveUI";
    public static String CARRYUI = "com.tencent.mm.plugin.remittance.ui.RemittanceDetailUI";    //转账页面

    public static String wechatversion;

    //LuckyMoneyNotHookReceiveUIMethodParameter 全局搜索 MicroMsg.NetSceneBase
    //handleLuckyMoney 全局搜索 add(".ui.transmit.SelectConversationUI")
    public static void SetValues() {
//        wechatversion = PropertiesUtils.getValue(RED_FILE, "wechatversion", "");
//        if (wechatversion.equals("8.0.18")) {
        LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
        OPEN_ID = "com.tencent.mm:id/giq";
        strredstatus = "com.tencent.mm:id/xs";
        chatredid = "com.tencent.mm:id/fhs";
        chatid = "com.tencent.mm:id/b4b";
        redcircle = "com.tencent.mm:id/kn6";
        chatnameid = "com.tencent.mm:id/hg4";
        msgredid = "com.tencent.mm:id/b47";
        msgredcontent = "com.tencent.mm:id/y0";
        msgisredid = "com.tencent.mm:id/y4";
        redunmsgcircle = "com.tencent.mm:id/a2f";
        msgname = "com.tencent.mm:id/ko4";

        chatonenameid = "com.tencent.mm:id/hg4";
        carrynum = "com.tencent.mm:id/yc";
        carrystates = "com.tencent.mm:id/y_";
        carrypagenum = "com.tencent.mm:id/imp";
        carrypagetime = "com.tencent.mm:id/imk";
        carrypagebtn = "com.tencent.mm:id/imh";
        redpagenum = "com.tencent.mm:id/gcq";
        userhead = "com.tencent.mm:id/b3s";
        chatitem = "com.tencent.mm:id/bth";

        redclose = "com.tencent.mm:id/k6i";
        carryclose = "com.tencent.mm:id/fz";

        LuckyMoneyNotHookReceiveUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
        LuckyMoneyNotHookReceiveUIMethod = "onSceneEnd";
        LuckyMoneyNotHookReceiveUIMethodParameter = "bc0.y";      //全局搜索getIsKinda
        LuckyMoneyNotHookReceiveUIButton = "o";                               //LuckyMoneyNotHookReceiveUI中的button
        handleLuckyMoney = "nh3.b";                               //全局搜索add(".ui.transmit.SelectConversationUI") Log.i("MicroMsg.PluginHelper", "start multi webview!!!!!!!!!")
        handleLuckyMoneyMethod = "h";
        handleLuckyMoneyClass = ".ui.LuckyMoneyNotHookReceiveUI";
//        }

//        HashMap hashMap = new HashMap();
//        areF = hashMap;
//        hashMap.put(FirebaseAnalytics.b.LOCATION, "talkroom");
//        areF.put("talkroom", "voip");
//        areG = new HashMap<>();
//        interceptors = new CopyOnWriteArrayList();
//        areH = new CopyOnWriteArrayList();
//        aogT = new String[]{WeChatHosts.domainString(b.a.aBhz), WeChatHosts.domainString(b.a.aBhD), WeChatHosts.domainString(b.a.aBhx)};
//        areI = new HashSet<String>() { // from class: com.tencent.mm.cc.d.2
//            {
//                add(".ui.transmit.SelectConversationUI");
//            }
//        };

    }

}
