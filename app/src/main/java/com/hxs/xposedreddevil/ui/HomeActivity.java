package com.hxs.xposedreddevil.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.model.VersionBean;
import com.hxs.xposedreddevil.utils.AssetsCopyTOSDcard;
import com.hxs.xposedreddevil.utils.GetAppVersion;
import com.hxs.xposedreddevil.utils.PackageManagerUtil;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;

public class HomeActivity extends AppCompatActivity {

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
        CheckPermissionInit();
        GetVersion();
        DataInit();
    }

    private void DataInit() {
        tvHomeUnroot.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font.ttf"));
        tvHomeRoot.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font.ttf"));
        AssetsCopyTOSDcard.Assets2Sd(this, "lucky_sound.mp3", Environment.getExternalStorageDirectory().toString() + "/xposedreddevil/lucky_sound.mp3");
        if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("7.0.0")) {
            spCenterVersion.setSelection(1);
        } else if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("6.7.3")) {
            spCenterVersion.setSelection(2);
        } else if (PropertiesUtils.getValue(RED_FILE, "wechatversion", "").equals("7.0.3")) {
            spCenterVersion.setSelection(0);
        }
        if (spCenterVersion.getSelectedItem().equals("7.0.0")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", "7.0.0");
        } else if (spCenterVersion.getSelectedItem().equals("6.7.3")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", "6.7.3");
        }else if (spCenterVersion.getSelectedItem().equals("7.0.3")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", "7.0.3");
        }
        if (!PackageManagerUtil.getItems(this).equals("")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", PackageManagerUtil.getItems(this));
        } else {
            WriteVersion();
        }
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

    private void GetVersion() {
        OkGo.<String>post("http://39.105.26.114:9672/redselectRedCode")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        VersionBean versionBean = new VersionBean();
                        if (response.body().contains("200")) {
                            versionBean = gson.fromJson(response.body(), VersionBean.class);
                            String s = versionBean.getData().getUpdateContent();
                            UpdataInit(versionBean.getData().getVersionCode(),
                                    s,
                                    versionBean.getData().getApkurl());
                        }
                    }
                });
    }

    /**
     * 更新操作
     *
     * @param version
     * @param updatamsg
     */
    public void UpdataInit(int version, String updatamsg, final String url) {
        if (version > Integer.parseInt(GetAppVersion.getVersionCode(HomeActivity.this))) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(HomeActivity.this);
            dialog.setTitle("发现新版本是否更新？");
            dialog.setMessage(updatamsg.replace("m", "\n"));
            dialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            dialog.setPositiveButton("更新", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                    startActivity(intent);
                }
            });
            dialog.show();
        }
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
