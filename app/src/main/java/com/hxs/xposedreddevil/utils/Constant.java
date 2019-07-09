package com.hxs.xposedreddevil.utils;

import android.os.Environment;

/**
 * @author ChayChan
 * @description: 定义常量
 * @date 2017/10/28  11:07
 */

public class Constant {
    public static final String RED_FILE = Environment.getExternalStorageDirectory() + "/xposedreddevil/reddevil_config.pro";
    public static final String TARGET_ID = "targetId";
    public static final String CONVERSATION_TYPE = "conversationType";
    public static final String TITLE = "title";
    public static final String WX_ROOT_PATH = "/data/data/com.tencent.mm/";                               // 微信根目录
    public static final String WX_SP_UIN_PATH = WX_ROOT_PATH + "shared_prefs/auth_info_key_prefs.xml";    // 微信保存uin的目录
    public static final String WX_DB_DIR_PATH = WX_ROOT_PATH + "MicroMsg/";                               // 微信保存聊天记录数据库的目录
    public static final String WX_DB_FILE_NAME = "EnMicroMsg.db";                                         // 微信聊天记录数据库

    public static final String WX_FILE_PATH = "/storage/emulated/0/Tencent/micromsg/";                    // 微信保存聊天时语音、图片、视频文件的地址

    //  public static final String currApkPath = "/data/data/com.dfsc.wechatrecord/"
    public static final String currApkPath = Environment.getExternalStorageDirectory() + "/xposedreddevil/";
    public static final String COPY_WX_DATA_DB = "wx_data.db";

    public static String uin = "";
    public static String dbPwd = "";                        // 数据库密码
    public static final String uinEnc = "";                       // 加密后的uin
}
