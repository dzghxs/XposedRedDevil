package com.hxs.xposedreddevil.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.base.BaseActivity;
import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.utils.AssetsCopyTOSDcard;
import com.hxs.xposedreddevil.utils.MessageEvent;
import com.hxs.xposedreddevil.utils.PackageManagerUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;
import static com.hxs.xposedreddevil.utils.Constant.currApkPath;

import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;

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
//d
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
        if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("8.0.38")) {
            spCenterVersion.setSelection(0);
        }
        if (spCenterVersion.getSelectedItem().equals("8.0.38")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", "8.0.38");
        }
        if (!PackageManagerUtil.getItems(this).equals("")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", PackageManagerUtil.getItems(this));
        } else {
            WriteVersion();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(MessageEvent msg) {
    }

    /**
     * 验证权限
     */
    public void CheckPermissionInit() {
        XXPermissions.with(this)
                .permission(Permission.Group.STORAGE)
                .permission(Permission.READ_PHONE_STATE)
                .request(new OnPermissionCallback() {

                    @Override
                    public void onGranted(List<String> permissions, boolean all) {

                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
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
                .setPositiveButton("确定", (dialog, which) -> dialog.dismiss());
    }

    @OnClick({R.id.home_card_unroot, R.id.home_card_root, R.id.iv_home_setting})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_home_setting:
                startActivity(new Intent(this, SettingActivity.class));
                break;
            case R.id.home_card_unroot:
//                Toast.makeText(this, "暂不提供免root抢红包操作，如需实现请自行编译代码", Toast.LENGTH_SHORT).show();
                Toast.makeText(this, "只支持8.0.32版本，其他版本自行编译代码", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(this, NotRootActivity.class));
                break;
            case R.id.home_card_root:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}
