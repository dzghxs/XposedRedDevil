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
    public static String OPEN_ID = "com.tencent.mm:id/cv0";   //开按钮
    public static String CARRYUI = "com.tencent.mm.plugin.remittance.ui.RemittanceDetailUI";    //转账页面

    public static String wechatversion;

    public static void SetValues() {
        wechatversion = PropertiesUtils.getValue(RED_FILE, "wechatversion", "");
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
            redunmsgcircle = "com.tencent.mm:id/azk";

            LuckyMoneyNotHookReceiveUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            LuckyMoneyNotHookReceiveUIMethod = "c";
            LuckyMoneyNotHookReceiveUIMethodParameter = "com.tencent.mm.ai.m";
            LuckyMoneyNotHookReceiveUIButton = "nTE";
            handleLuckyMoney = "com.tencent.mm.bs.d";
            handleLuckyMoneyMethod = "b";
            handleLuckyMoneyClass = ".ui.LuckyMoneyNotHookReceiveUI";

        } else if (wechatversion.equals("7.0.13")) {
            LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            OPEN_ID = "com.tencent.mm:id/dan";
            strredstatus = "com.tencent.mm:id/aul";
            chatredid = "com.tencent.mm:id/bal";
            chatid = "com.tencent.mm:id/bah";
            redcircle = "com.tencent.mm:id/op";
            chatnameid = "com.tencent.mm:id/baj";
            msgredid = "com.tencent.mm:id/atb";
            msgredcontent = "com.tencent.mm:id/auk";
            msgisredid = "com.tencent.mm:id/aum";
            redunmsgcircle = "com.tencent.mm:id/bai";
            msgname = "com.tencent.mm:id/lt";

            chatonenameid = "com.tencent.mm:id/b9i";
            carrynum = "com.tencent.mm:id/ata";
            carrystates = "com.tencent.mm:id/atb";
            carrypagenum = "com.tencent.mm:id/e71";
            carrypagetime = "com.tencent.mm:id/e81";
            carrypagebtn = "com.tencent.mm:id/e7x";
            redpagenum = "com.tencent.mm:id/czp";

            LuckyMoneyNotHookReceiveUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            LuckyMoneyNotHookReceiveUIMethod = "onSceneEnd";
            LuckyMoneyNotHookReceiveUIMethodParameter = "com.tencent.mm.al.n";
            LuckyMoneyNotHookReceiveUIButton = "sxm";
            handleLuckyMoney = "com.tencent.mm.bs.d";
            handleLuckyMoneyMethod = "b";
            handleLuckyMoneyClass = ".ui.LuckyMoneyNotHookReceiveUI";
        } else if (wechatversion.equals("7.0.14")) {
            LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            OPEN_ID = "com.tencent.mm:id/dan";
            strredstatus = "com.tencent.mm:id/aul";
            chatredid = "com.tencent.mm:id/bal";
            chatid = "com.tencent.mm:id/bah";
            redcircle = "com.tencent.mm:id/op";
            chatnameid = "com.tencent.mm:id/baj";
            msgredid = "com.tencent.mm:id/atb";
            msgredcontent = "com.tencent.mm:id/auk";
            msgisredid = "com.tencent.mm:id/aum";
            redunmsgcircle = "com.tencent.mm:id/bai";
            msgname = "com.tencent.mm:id/lt";

            chatonenameid = "com.tencent.mm:id/b9i";
            carrynum = "com.tencent.mm:id/ata";
            carrystates = "com.tencent.mm:id/atb";
            carrypagenum = "com.tencent.mm:id/e71";
            carrypagetime = "com.tencent.mm:id/e81";
            carrypagebtn = "com.tencent.mm:id/e7x";
            redpagenum = "com.tencent.mm:id/czp";

            LuckyMoneyNotHookReceiveUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            LuckyMoneyNotHookReceiveUIMethod = "onSceneEnd";
            LuckyMoneyNotHookReceiveUIMethodParameter = "com.tencent.mm.al.n";
            LuckyMoneyNotHookReceiveUIButton = "sJD";
            handleLuckyMoney = "com.tencent.mm.bs.d";
            handleLuckyMoneyMethod = "b";
            handleLuckyMoneyClass = ".ui.LuckyMoneyNotHookReceiveUI";
        }
    }

}
