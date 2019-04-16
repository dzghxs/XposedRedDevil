package com.hxs.xposedreddevil.utils;

import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;

import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;

public class AcxiliaryServiceStaticValues {

    public static String strredstatus = "";   //红包状态
    public static String chatredid = "";      //聊天列表红包消息ID
    public static String chatid = "";         //聊天item ID
    public static String redcircle = "";      //消息小红点ID
    public static String redunmsgcircle = ""; //消息小红点ID(设置为免打扰)
    public static String chatnameid = "";     //聊天对象ID
    public static String msgredid = "";       //聊天页面红包ID
    public static String msgredcontent = "";  //聊天页面红包内容ID(恭喜发财，大吉大利)
    public static String msgisredid = "";     //微信红包下方微信红包四个字

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
            redunmsgcircle = "com.tencent.mm:id/azk";
        }
    }

}
