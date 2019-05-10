package com.hxs.xposedreddevil.greendao.db;

import android.content.Context;

import com.hxs.xposedreddevil.greendao.WxChatInvokeMsg;
import com.hxs.xposedreddevil.greendao.WxChatInvokeMsgDao;

import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.query.QueryBuilder;

import java.util.List;

/**
 * Created by zed on 2018/5/8.
 */
public class WxChatInvokeMsgDB {
    /**
     * 添加数据至数据库  
     *
     * @param context
     * @param stu
     */
    public static void insertData(Context context, WxChatInvokeMsg stu) {
        DbManager.getDaoSession(context).getWxChatInvokeMsgDao().insert(stu);
    }


    /**
     * 将数据实体通过事务添加至数据库  
     *
     * @param context
     * @param list
     */
    public static void insertData(Context context, List<WxChatInvokeMsg> list) {
        if (null == list || list.size() <= 0) {
            return;
        }
        DbManager.getDaoSession(context).getWxChatInvokeMsgDao().insertInTx(list);
    }

    /**
     * 添加数据至数据库，如果存在，将原来的数据覆盖  
     * 内部代码判断了如果存在就update(entity);不存在就insert(entity)；  
     *
     * @param context
     * @param WxChatInvokeMsg
     */
    public static void saveData(Context context, WxChatInvokeMsg WxChatInvokeMsg) {
        DbManager.getDaoSession(context).getWxChatInvokeMsgDao().save(WxChatInvokeMsg);
    }

    /**
     * 删除数据至数据库  
     *
     * @param context
     * @param WxChatInvokeMsg 删除具体内容  
     */
    public static void deleteData(Context context, WxChatInvokeMsg WxChatInvokeMsg) {
        DbManager.getDaoSession(context).getWxChatInvokeMsgDao().delete(WxChatInvokeMsg);
    }


    /**
     * 删除全部数据  
     *
     * @param context
     */
    public static void deleteAllData(Context context) {
        DbManager.getDaoSession(context).getWxChatInvokeMsgDao().deleteAll();
    }

    /**
     * 更新数据库  
     *
     * @param context
     * @param WxChatInvokeMsg
     */
    public static void updateData(Context context, WxChatInvokeMsg WxChatInvokeMsg) {
        DbManager.getDaoSession(context).getWxChatInvokeMsgDao().update(WxChatInvokeMsg);
    }


    /**
     * 查询所有数据  
     *
     * @param context
     * @return
     */
    public static List<WxChatInvokeMsg> queryAll(Context context) {
        QueryBuilder<WxChatInvokeMsg> builder = DbManager.getDaoSession(context).getWxChatInvokeMsgDao().queryBuilder();

        return builder.build().list();
    }

    /**
     * 查询所有数据
     *
     * @param context
     * @return
     */
    public static WxChatInvokeMsg queryByMsgId(Context context, String msgId) {
        QueryBuilder<WxChatInvokeMsg> builder = DbManager.getDaoSession(context).getWxChatInvokeMsgDao().queryBuilder();
        Query<WxChatInvokeMsg> build = builder.where(WxChatInvokeMsgDao.Properties.MsgId.eq(msgId)).build();
        return build.unique();
    }


    /**
     *  分页加载  
     * @param context
     * @param pageSize 当前第几页(程序中动态修改pageSize的值即可)  
     * @param pageNum  每页显示多少个  
     * @return
     */
    public static List<WxChatInvokeMsg> queryPaging(int pageSize, int pageNum, Context context){
        WxChatInvokeMsgDao WxChatInvokeMsgDao = DbManager.getDaoSession(context).getWxChatInvokeMsgDao();
        List<WxChatInvokeMsg> listMsg = WxChatInvokeMsgDao.queryBuilder()
                .offset(pageSize * pageNum).limit(pageNum).list();
        return listMsg;
    }
}
