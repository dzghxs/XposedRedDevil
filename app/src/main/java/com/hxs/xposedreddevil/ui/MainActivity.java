package com.hxs.xposedreddevil.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Switch;
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
import com.hxs.xposedreddevil.utils.MessageEvent;
import com.hxs.xposedreddevil.utils.PackageManagerUtil;
import com.hxs.xposedreddevil.utils.PushUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.InputStream;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import skin.support.SkinCompatManager;

public class MainActivity extends AppCompatActivity {

    public static final String RED_FILE = Environment.getExternalStorageDirectory()+"/xposedreddevil/reddevil_config.pro";

    @BindView(R.id.ll_version)
    LinearLayout llVersion;
    @BindView(R.id.sp_center_version)
    Spinner spCenterVersion;
    @BindView(R.id.sw_noroot_main)
    Switch swNorootMain;
    @BindView(R.id.sw_main)
    Switch swMain;
    @BindView(R.id.sw_own)
    Switch swOwn;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.sw_sleep)
    Switch swSleep;
    @BindView(R.id.et_sleep)
    EditText etSleep;
    @BindView(R.id.ll_sleep)
    LinearLayout llSleep;
    @BindView(R.id.sw_sound)
    Switch swSound;
    @BindView(R.id.sw_push)
    Switch swPush;
    @BindView(R.id.tv_star)
    TextView tvStar;
    @BindView(R.id.tv_skin)
    TextView tvSkin;
    @BindView(R.id.ll_skin)
    LinearLayout llSkin;
    @BindView(R.id.ll_add_no)
    LinearLayout llAddNo;

    Gson gson = new Gson();

    private final String payCode = "FKX03573LOMYIBUT6ERCF1";

    private LinearLayout llLight;
    private RadioButton rbLight;
    private View vLight;
    private LinearLayout llNight;
    private RadioButton rbNight;
    private View vNight;
    private TextView tvDismiss;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        CheckPermissionInit();
        GetVersion();
        SwitchClickInit();
    }

    private void SwitchClickInit() {
        AssetsCopyTOSDcard.Assets2Sd(this, "lucky_sound.mp3", Environment.getExternalStorageDirectory().toString() + "/xposedreddevil/lucky_sound.mp3");
        tvSkin.setText(PropertiesUtils.getValue(RED_FILE, "redskin", "亮色"));
        if (spCenterVersion.getSelectedItem().equals("7.0.0")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", "7.0.0");
        } else if (spCenterVersion.getSelectedItem().equals("6.7.3")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", "6.7.3");
        }
        if (!PackageManagerUtil.getItems(MainActivity.this).equals("")) {
            PropertiesUtils.putValue(RED_FILE, "wechatversion", PackageManagerUtil.getItems(this));
        } else {
            WriteVersion();
        }
        try {
            if (PropertiesUtils.getValue(RED_FILE, "rednorootmain", "2").equals("1")) {
                if (!PackageManagerUtil.isAccessibilitySettingsOn(this)) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                }
                swNorootMain.setChecked(true);
            } else {
                swNorootMain.setChecked(false);
            }
            if (PropertiesUtils.getValue(RED_FILE, "redmain", "2").equals("1")) {
                swMain.setChecked(true);
            } else {
                swMain.setChecked(false);
            }
            if (PropertiesUtils.getValue(RED_FILE, "red", "2").equals("1")) {
                swOwn.setChecked(true);
            } else {
                swOwn.setChecked(false);
            }
            if (PropertiesUtils.getValue(RED_FILE, "sleep", "2").equals("1")) {
                swSleep.setChecked(true);
                llSleep.setVisibility(View.VISIBLE);
            } else {
                swSleep.setChecked(false);
                llSleep.setVisibility(View.GONE);
            }
            if (PropertiesUtils.getValue(RED_FILE, "sound", "2").equals("1")) {
                swSound.setChecked(true);
            } else {
                swSound.setChecked(false);
            }
            if (PropertiesUtils.getValue(RED_FILE, "push", "2").equals("1")) {
                swPush.setChecked(true);
            } else {
                swPush.setChecked(false);
            }
            etSleep.setText(PropertiesUtils.getValue(RED_FILE, "sleeptime", "1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        swNorootMain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    if (!PackageManagerUtil.isAccessibilitySettingsOn(MainActivity.this)) {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    }
                    PropertiesUtils.putValue(RED_FILE, "rednorootmain", "1");
                } else {
                    PropertiesUtils.putValue(RED_FILE, "rednorootmain", "2");
                }
            }
        });
        swMain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PropertiesUtils.putValue(RED_FILE, "redmain", "1");
                } else {
                    PropertiesUtils.putValue(RED_FILE, "redmain", "2");
                }
            }
        });
        swOwn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PropertiesUtils.putValue(RED_FILE, "red", "1");
                } else {
                    PropertiesUtils.putValue(RED_FILE, "red", "2");
                }
            }
        });
        swSleep.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PropertiesUtils.putValue(RED_FILE, "sleep", "1");
                    llSleep.setVisibility(View.VISIBLE);
                } else {
                    PropertiesUtils.putValue(RED_FILE, "sleep", "2");
                    llSleep.setVisibility(View.GONE);
                }
            }
        });
        swSound.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PropertiesUtils.putValue(RED_FILE, "sound", "1");
                } else {
                    PropertiesUtils.putValue(RED_FILE, "sound", "2");
                }
            }
        });
        swPush.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (b) {
                    PropertiesUtils.putValue(RED_FILE, "push", "1");
                } else {
                    PropertiesUtils.putValue(RED_FILE, "push", "2");
                }
            }
        });
        etSleep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("0")) {
                    PropertiesUtils.putValue(RED_FILE, "sleeptime", "1");
                } else {
                    PropertiesUtils.putValue(RED_FILE, "sleeptime", editable.toString());
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getMsg(MessageEvent messages) {
        PushUtils.showNotification(this, "private", "25", "微信", "天降红包");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    /**
     * 需要提前准备好 微信收款码 照片，可通过微信客户端生成
     * wxp://f2f0j1REHFC8YJor7UUsS6N-1PZiFE2mhOht
     */
    private void donateWeixin() {
        InputStream weixinQrIs = getResources().openRawResource(R.raw.mm_facetoface_collect_qrcode_1536027137680);
        String qrPath = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "AndroidDonateSample" + File.separator +
                "weixin.png";
        WeiXinDonate.saveDonateQrImage2SDCard(qrPath, BitmapFactory.decodeStream(weixinQrIs));
        WeiXinDonate.donateViaWeiXin(this, qrPath);
    }

    /**
     * 支付宝支付
     *
     * @param payCode 收款码后面的字符串；例如：收款二维码里面的字符串为 HTTPS://QR.ALIPAY.COM/FKX03133TLJFCY8UNXHC56 ，则
     *                payCode = FKX03133TLJFCY8UNXHC56
     *                注：不区分大小写
     */
    private void donateAlipay(String payCode) {
        boolean hasInstalledAlipayClient = AlipayDonate.hasInstalledAlipayClient(this);
        if (hasInstalledAlipayClient) {
            AlipayDonate.startAlipayClient(this, payCode);
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
                        Toast.makeText(MainActivity.this, "必要权限未提供，app即将关闭", Toast.LENGTH_SHORT).show();
                        try {
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        finish();
                    }
                });
    }

    private void WriteVersion(){
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
        if (version > Integer.parseInt(GetAppVersion.getVersionCode(MainActivity.this))) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setTitle("发现新版本是否更新？");
            dialog.setMessage(updatamsg.replace("m","\n"));
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

    /**
     * 选择主题
     */
    private void SelectSkinInit() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_skin_layout, null);
        initView(v);
        AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
        dialog.setView(v);
        final AlertDialog d = dialog.create();
        llLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 恢复应用默认皮肤
                SkinCompatManager.getInstance().restoreDefaultTheme();
                PropertiesUtils.putValue(RED_FILE, "redskin", "亮色");
                rbLight.setChecked(true);
                rbNight.setChecked(false);
                tvSkin.setText("亮色");
                d.dismiss();
            }
        });
        vLight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        llNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SkinCompatManager.getInstance().loadSkin("night.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
                PropertiesUtils.putValue(RED_FILE, "redskin", "暗色");
                rbLight.setChecked(false);
                rbNight.setChecked(true);
                tvSkin.setText("暗色");
                d.dismiss();
            }
        });
        vNight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        tvDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });
        d.show();
    }

    private void initView(View v) {
        llLight = v.findViewById(R.id.ll_light);
        rbLight = v.findViewById(R.id.rb_light);
        vLight = v.findViewById(R.id.v_light);
        llNight = v.findViewById(R.id.ll_night);
        rbNight = v.findViewById(R.id.rb_night);
        vNight = v.findViewById(R.id.v_night);
        tvDismiss = v.findViewById(R.id.tv_dismiss);
        rbLight.setFocusable(false);
        rbNight.setFocusable(false);
        if (PropertiesUtils.getValue(RED_FILE, "redskin", "亮色").equals("亮色")) {
            rbLight.setChecked(true);
            rbNight.setChecked(false);
        } else {
            rbLight.setChecked(false);
            rbNight.setChecked(true);
        }
    }

    //判断系统是否设置了默认浏览器
    public boolean hasPreferredApplication(Context context, Intent intent) {
        PackageManager pm = context.getPackageManager();
        ResolveInfo info = pm.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
        return !"android".equals(info.activityInfo.packageName);
    }

    @OnClick({R.id.tv_pay, R.id.tv_star, R.id.ll_skin, R.id.ll_add_no})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_pay:
                new AlertDialog.Builder(MainActivity.this)
                        .setTitle("去捐赠")
                        .setNegativeButton("微信", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                donateWeixin();
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("支付宝", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                donateAlipay(payCode);
                                dialog.dismiss();
                            }
                        })
                        .create().show();
                break;
            case R.id.tv_star:
                try {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse("https://github.com/dzghxs/XposedRedDevil");//splitflowurl为分流地址
                    intent.setData(content_url);
                    if (!hasPreferredApplication(MainActivity.this, intent)) {
                        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                    }
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case R.id.ll_skin:
                SelectSkinInit();
                break;
            case R.id.ll_add_no:
                startActivity(new Intent(this,SelectFilterActivity.class));
                break;
        }
    }

}
