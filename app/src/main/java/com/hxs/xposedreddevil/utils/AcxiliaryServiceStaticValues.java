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
    public static String chatnameid = "";     //聊天对象ID（聊天列表用户名称id）
    public static String msgredid = "";       //聊天页面红包ID（聊天页面红包id）
    public static String msgredcontent = "";  //聊天页面红包内容ID(恭喜发财，大吉大利)
    public static String msgisredid = "";     //微信红包下方微信红包四个字
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

    public static String wechatversion;

    public static void SetValues(){
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
            handleLuckyMoney = "com.tencent.mm.bp.d";
            handleLuckyMoneyMethod = "b";
            handleLuckyMoneyClass = ".ui.LuckyMoneyNotHookReceiveUI";

        }  else if (wechatversion.equals("7.0.6")) {
            LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            OPEN_ID = "com.tencent.mm:id/d5a";
            strredstatus = "com.tencent.mm:id/aso";
            chatredid = "com.tencent.mm:id/b9k";
            chatid = "com.tencent.mm:id/b9g";
            redcircle = "com.tencent.mm:id/o0";
            chatnameid = "com.tencent.mm:id/b95";
            msgredid = "com.tencent.mm:id/ard";
            msgredcontent = "com.tencent.mm:id/asn";
            msgisredid = "com.tencent.mm:id/asp";
            redunmsgcircle = "com.tencent.mm:id/b9h";

            LuckyMoneyNotHookReceiveUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            LuckyMoneyNotHookReceiveUIMethod = "onSceneEnd";
            LuckyMoneyNotHookReceiveUIMethodParameter = "com.tencent.mm.ak.m";
            LuckyMoneyNotHookReceiveUIButton = "oNE";
            handleLuckyMoney = "com.tencent.mm.bs.d";
            handleLuckyMoneyMethod = "a";
            handleLuckyMoneyClass = ".ui.LuckyMoneyNotHookReceiveUI";

        } else if (wechatversion.equals("7.0.5")) {
            LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            OPEN_ID = "com.tencent.mm:id/d4h";
            strredstatus = "com.tencent.mm:id/asa";
            chatredid = "com.tencent.mm:id/b97";
            chatid = "com.tencent.mm:id/b93";
            redcircle = "com.tencent.mm:id/nz";
            chatnameid = "com.tencent.mm:id/b95";
            msgredid = "com.tencent.mm:id/ar0";
            msgredcontent = "com.tencent.mm:id/as_";
            msgisredid = "com.tencent.mm:id/asb";
            redunmsgcircle = "com.tencent.mm:id/b94";

            LuckyMoneyNotHookReceiveUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            LuckyMoneyNotHookReceiveUIMethod = "onSceneEnd";
            LuckyMoneyNotHookReceiveUIMethodParameter = "com.tencent.mm.ai.m";
            LuckyMoneyNotHookReceiveUIButton = "okg";
            handleLuckyMoney = "com.tencent.mm.bq.d";
            handleLuckyMoneyMethod = "b";
            handleLuckyMoneyClass = ".ui.LuckyMoneyNotHookReceiveUI";

        } else if (wechatversion.equals("7.0.4")) {
            LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            OPEN_ID = "com.tencent.mm:id/d02";
            strredstatus = "com.tencent.mm:id/aqk";
            chatredid = "com.tencent.mm:id/b6g";
            chatid = "com.tencent.mm:id/b6c";
            redcircle = "com.tencent.mm:id/nf";
            chatnameid = "com.tencent.mm:id/b6e";
            msgredid = "com.tencent.mm:id/ap9";
            msgredcontent = "com.tencent.mm:id/aqj";
            msgisredid = "com.tencent.mm:id/aql";
            redunmsgcircle = "com.tencent.mm:id/b6d";

            LuckyMoneyNotHookReceiveUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            LuckyMoneyNotHookReceiveUIMethod = "c";
            LuckyMoneyNotHookReceiveUIMethodParameter = "com.tencent.mm.ai.m";
            LuckyMoneyNotHookReceiveUIButton = "nTE";
            handleLuckyMoney = "com.tencent.mm.bp.d";
            handleLuckyMoneyMethod = "b";
            handleLuckyMoneyClass = ".ui.LuckyMoneyNotHookReceiveUI";

        } else if (wechatversion.equals("7.0.3")) {
            LUCKEY_MONEY_RECEIVER = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            OPEN_ID = "com.tencent.mm:id/cyf";
            strredstatus = "com.tencent.mm:id/aq6";
            chatredid = "com.tencent.mm:id/b5q";
            chatid = "com.tencent.mm:id/b5m";
            redcircle = "com.tencent.mm:id/mv";
            chatnameid = "com.tencent.mm:id/b5o";
            msgredid = "com.tencent.mm:id/aou";
            msgredcontent = "com.tencent.mm:id/aq5";
            msgisredid = "com.tencent.mm:id/aq7";
            redunmsgcircle = "com.tencent.mm:id/b5n";

            LuckyMoneyNotHookReceiveUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            LuckyMoneyNotHookReceiveUIMethod = "c";
            LuckyMoneyNotHookReceiveUIMethodParameter = "com.tencent.mm.ah.m";
            LuckyMoneyNotHookReceiveUIButton = "nqy";
            handleLuckyMoney = "com.tencent.mm.br.d";
            handleLuckyMoneyMethod = "b";
            handleLuckyMoneyClass = ".ui.LuckyMoneyNotHookReceiveUI";

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
            redunmsgcircle = "com.tencent.mm:id/b4n";

            LuckyMoneyNotHookReceiveUI = "com.tencent.mm.plugin.luckymoney.ui.LuckyMoneyNotHookReceiveUI";
            LuckyMoneyNotHookReceiveUIMethod = "c";
            LuckyMoneyNotHookReceiveUIMethodParameter = "com.tencent.mm.ah.m";
            LuckyMoneyNotHookReceiveUIButton = "nai";
            handleLuckyMoney = "com.tencent.mm.br.d";
            handleLuckyMoneyMethod = "b";
            handleLuckyMoneyClass = ".ui.LuckyMoneyNotHookReceiveUI";

        }
    }

}
