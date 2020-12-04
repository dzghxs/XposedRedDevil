package com.hxs.xposedreddevil.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Vibrator;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.tts.client.SpeechSynthesizer;
import com.baidu.tts.client.SpeechSynthesizerListener;
import com.baidu.tts.client.TtsMode;
import com.google.gson.Gson;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.base.BaseActivity;
import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.control.InitConfig;
import com.hxs.xposedreddevil.control.MySyntherizer;
import com.hxs.xposedreddevil.control.NonBlockSyntherizer;
import com.hxs.xposedreddevil.greendao.DbCarryList;
import com.hxs.xposedreddevil.listener.UiMessageListener;
import com.hxs.xposedreddevil.utils.AssetsCopyTOSDcard;
import com.hxs.xposedreddevil.utils.AutoCheck;
import com.hxs.xposedreddevil.utils.MessageEvent;
import com.hxs.xposedreddevil.utils.OfflineResource;
import com.hxs.xposedreddevil.utils.PackageManagerUtil;
import com.hxs.xposedreddevil.utils.SQLiteUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.carrypagenum;
import static com.hxs.xposedreddevil.utils.AcxiliaryServiceStaticValues.carrypagetime;
import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;
import static com.hxs.xposedreddevil.utils.Constant.currApkPath;

public class HomeActivity extends BaseActivity {

    @BindView(R.id.iv_home_setting)
    ImageView ivHomeSetting;
    @BindView(R.id.sp_center_version)
    Spinner spCenterVersion;
    @BindView(R.id.ll_version)
    LinearLayout llVersion;
    @BindView(R.id.home_card_unroot)
    CardView homeCardUnroot;
    @BindView(R.id.home_card_root)
    CardView homeCardRoot;
    @BindView(R.id.tv_home_unroot)
    TextView tvHomeUnroot;
    @BindView(R.id.tv_home_root)
    TextView tvHomeRoot;

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        PropertiesUtils.putValue(RED_FILE, "widthPixels", widthPixels + "");
        CheckPermissionInit();
        DataInit();
    }

    @Override
    protected void onDestroy() {
//        synthesizer.release();
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    private void DataInit() {
        File file = new File(currApkPath + "ListTime");
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir();
        }
        tvHomeUnroot.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font.ttf"));
        tvHomeRoot.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font.ttf"));
        AssetsCopyTOSDcard.Assets2Sd(this, "lucky_sound.mp3", Environment.getExternalStorageDirectory().toString() + "/xposedreddevil/lucky_sound.mp3");
        if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("7.0.21")) {
            spCenterVersion.setSelection(0);
        } else if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("7.0.20")) {
            spCenterVersion.setSelection(1);
        }
        if (spCenterVersion.getSelectedItem().equals("7.0.21")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", "7.0.21");
        } else if (spCenterVersion.getSelectedItem().equals("7.0.20")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", "7.0.20");
        }
        if (!PackageManagerUtil.getItems(this).equals("")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", PackageManagerUtil.getItems(this));
        } else {
            WriteVersion();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(MessageEvent msg) {
        if (msg.getMessage().contains("@")) {
        }
    }

    private void checkResult(int result, String method) {
        if (result != 0) {
            toPrint("error code :" + result + " method:" + method + ", 错误码文档:http://yuyin.baidu.com/docs/tts/122 ");
        }
    }

    protected void toPrint(String str) {
        Message msg = Message.obtain();
        msg.obj = str;
        mainHandler.sendMessage(msg);
    }

    /**
     * 验证权限
     */
    public void CheckPermissionInit() {
        XXPermissions.with(this)
                .permission(Permission.Group.STORAGE)
                .permission(Permission.READ_PHONE_STATE)
                .constantRequest() //可设置被拒绝后继续申请，直到用户授权或者永久拒绝
                .request(new OnPermission() {

                    @Override
                    public void hasPermission(List<String> granted, boolean isAll) {
                    }

                    @Override
                    public void noPermission(List<String> denied, boolean quick) {
                        Toast.makeText(HomeActivity.this, "必要权限未提供，app即将关闭", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
    }

    private void WriteVersion() {
        new AlertDialog.Builder(this).setTitle("获取微信版本失败，请手动选择微信版本")
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
    }

    @OnClick({R.id.home_card_unroot, R.id.home_card_root, R.id.iv_home_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_home_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.home_card_unroot:
                startActivity(new Intent(this, NotRootActivity.class));
                break;
            case R.id.home_card_root:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
