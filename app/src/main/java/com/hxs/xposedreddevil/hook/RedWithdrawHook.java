package com.hxs.xposedreddevil.hook;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.graphics.Color;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.greendao.WxChatInvokeMsg;
import com.hxs.xposedreddevil.greendao.db.WxChatInvokeMsgDB;
import com.hxs.xposedreddevil.utils.ShapeUtil;

import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XC_MethodReplacement;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class RedWithdrawHook {

    private XC_LoadPackage.LoadPackageParam classLoader;

    public RedWithdrawHook() {
    }

    public void init(XC_LoadPackage.LoadPackageParam classLoader) {
        if (this.classLoader == null) {
            this.classLoader = classLoader;
            hook(classLoader);
        }
    }

    public static RedWithdrawHook getInstance() {
        return RedWithdrawHookHolder.instance;
    }

    private static class RedWithdrawHookHolder {
        @SuppressLint("StaticFieldLeak")
        private static final RedWithdrawHook instance = new RedWithdrawHook();
    }

    private void hook(final XC_LoadPackage.LoadPackageParam lpparam) {
        if (lpparam.packageName.contains("com.tencent.mm")) {
            XposedHelpers.findAndHookMethod(Application.class, "attach", Context.class, new XC_MethodHook() {

                @Override
                protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                    super.afterHookedMethod(param);
                    Context context = (Context) param.args[0];
                    hookWxChatUIMM(context, lpparam.classLoader);
                    hookDB(context, lpparam.classLoader);
                }
            });
        }
    }

    /* 直接hook sql达到获取撤回消息id的目的
     *
     * @param applicationContext
     * @param classLoader
     */
    private void hookDB(final Context applicationContext, final ClassLoader classLoader) {
        final Class<?> sQLiteDatabase = XposedHelpers.findClass("com.tencent.wcdb.database.SQLiteDatabase", classLoader);
        final Class<?> cancellationSignal = XposedHelpers.findClass("com.tencent.wcdb.support.CancellationSignal", classLoader);
        if (sQLiteDatabase == null) return;
        XposedHelpers.findAndHookConstructor("com.tencent.wcdb.database.SQLiteProgram",
                classLoader,
                sQLiteDatabase,
                String.class,
                Object[].class,
                cancellationSignal,
                new XC_MethodReplacement() {
                    @Override
                    protected Object replaceHookedMethod(MethodHookParam param) throws Throwable {
                        Object[] objArr = (Object[]) param.args[2];
                        String originalSql = param.args[1].toString();
                        //打印所有调用SQLiteProgram的sql
                        //LogUtils.e("hookDB", "sql -> " + param.args[1], "objArr:" + JSON.toJSONString(objArr));
                        if (objArr != null && originalSql.toUpperCase().startsWith("UPDATE MESSAGE")) {
                            for (Object obj : objArr) {
                                String sqlParam = obj.toString();//自己撤回10002 别人撤回10000
                                if (sqlParam.equals("10000")) {//别人撤回
                                    Object[] newObjArr = new Object[2];
                                    //param.args[1] = "UPDATE message SET type=? WHERE msgId=?";
                                    param.args[1] = "select * from message where type=? and msgId=?";
                                    param.args[2] = newObjArr;
                                    newObjArr[0] = 1;
                                    newObjArr[1] = objArr[objArr.length - 1];
                                    //param.args[1] = "UPDATE message SET content=(select (select content from message where msgId = ?)||X'0D'||X'0A'||X'0D'||X'0A'||(\"<sysmsg>wxInvoke卧槽，TA竟然要撤回上面的信息wxInvoke</sysmsg>\")),msgId=?,type=? WHERE msgId=?";
                                    WxChatInvokeMsg msg = new WxChatInvokeMsg();
                                    msg.setMsgId(newObjArr[1].toString());
                                    WxChatInvokeMsgDB.insertData(applicationContext, msg);
                                }
                            }
                        }
                        return XposedBridge.invokeOriginalMethod(param.method, param.thisObject, param.args);
                    }
                });
    }

    /**
     * 微信聊天界面
     *
     * @param applicationContext
     * @param classLoader
     */

    private void hookWxChatUIMM(final Context applicationContext, final ClassLoader classLoader) {
        XposedHelpers.findAndHookMethod("com.tencent.mm.ui.base.MMPullDownView",
                classLoader,
                "onLayout",
                boolean.class,
                int.class,
                int.class,
                int.class,
                int.class,
                new XC_MethodHook() {
                    @Override
                    protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                        super.beforeHookedMethod(param);
                        ViewGroup mMPullDownView = (ViewGroup) param.thisObject;
//                        if (mMPullDownView.getVisibility() == View.GONE) return;
                        for (int i = 0; i < mMPullDownView.getChildCount(); i++) {
                            View childAt = mMPullDownView.getChildAt(i);
                            if (childAt instanceof ListView) {
                                final ListView listView = (ListView) childAt;
                                final ListAdapter adapter = listView.getAdapter();
                                XposedHelpers.findAndHookMethod(adapter.getClass(),
                                        "getView",
                                        int.class,
                                        View.class,
                                        ViewGroup.class,
                                        new XC_MethodHook() {
                                            @Override
                                            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                                                super.beforeHookedMethod(param);
                                                int position = (int) param.args[0];
                                                View view = (View) param.args[1];
                                                JSONObject itemData = null;
//                                                LogUtils.i(position, view.toString());
                                                if (position < adapter.getCount()) {
                                                    itemData = JSON.parseObject(JSON.toJSONString(adapter.getItem(position)), JSONObject.class);
                                                    //经过以上代码可以知道    itemViewType == 1的时候打印的值是正常对话列表的值
                                                    if (itemData != null && (view != null && view.toString().contains("com.tencent.mm.ui.chatting.viewitems.v"))) {
//                                                        if (itemData != null && itemViewType == 1 && (view != null && view.toString().contains("com.tencent.mm.ui.chatting.viewitems.p"))) {
                                                        String field_msgId = itemData.getString("field_msgId");
                                                        WxChatInvokeMsg wxChatInvokeMsg = WxChatInvokeMsgDB.queryByMsgId(applicationContext, field_msgId);
                                                        ViewGroup itemView = (ViewGroup) view;
                                                        View itemViewChild = itemView.getChildAt(0);
                                                        Object tag = itemViewChild.getTag(R.id.wx_parent_has_invoke_msg);
                                                        TextView textView;
                                                        if (tag == null) {
                                                            textView = new TextView(applicationContext);
                                                            textView.setGravity(Gravity.CENTER);
                                                            textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                                                            itemViewChild.setTag(R.id.wx_parent_has_invoke_msg, textView);
                                                            textView.setId(R.id.wx_invoke_msg);
                                                            itemView.addView(textView);
                                                        } else {
                                                            textView = (TextView) itemViewChild.getTag(R.id.wx_parent_has_invoke_msg);
                                                        }
                                                        textView.setText("");
                                                        textView.setVisibility(View.GONE);
                                                        if (wxChatInvokeMsg != null) {
                                                            textView.setPadding(10, 5, 10, 5);
                                                            textView.setBackgroundDrawable(ShapeUtil.getCornerDrawable());
                                                            View msgView = itemView.getChildAt(3);
                                                            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) textView.getLayoutParams();
                                                            lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
                                                            lp.addRule(RelativeLayout.BELOW, msgView.getId());
                                                            lp.bottomMargin = 50;
                                                            textView.setText(Html.fromHtml("<font color='#FF0000'>想</font><font color='#FF6600'>撤</font><font color='#FFFF00'>回</font><font color='#008000'>消</font><font color='#00FFFF'>息</font><font color='#0000FF'>？</font><font color='#800080'>biubiubiu</font>"));
                                                            textView.setVisibility(View.VISIBLE);
                                                        }
                                                    }
                                                }
//

                                            }

                                        });
                                break;

                            }
                        }
                    }
                });
    }


}
