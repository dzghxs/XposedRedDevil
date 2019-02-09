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
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.model.VersionBean;
import com.hxs.xposedreddevil.utils.GetAppVersion;
import com.hxs.xposedreddevil.weight.LoadingDialog;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import java.io.File;
import java.io.InputStream;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import skin.support.SkinCompatManager;

import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;

public class SettingActivity extends AppCompatActivity {

    @BindView(R.id.tv_skin)
    TextView tvSkin;
    @BindView(R.id.ll_skin)
    LinearLayout llSkin;
    @BindView(R.id.tv_update)
    TextView tvUpdate;
    @BindView(R.id.tv_pay)
    TextView tvPay;
    @BindView(R.id.tv_star)
    TextView tvStar;

    private LinearLayout llLight;
    private RadioButton rbLight;
    private View vLight;
    private LinearLayout llNight;
    private RadioButton rbNight;
    private View vNight;
    private TextView tvDismiss;

    private final String payCode = "FKX03573LOMYIBUT6ERCF1";

    Gson gson = new Gson();

    LoadingDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        dialog = new LoadingDialog(this);
        tvSkin.setText(PropertiesUtils.getValue(RED_FILE, "redskin", "亮色"));
    }

    /**
     * 选择主题
     */
    private void SelectSkinInit() {
        View v = LayoutInflater.from(this).inflate(R.layout.dialog_skin_layout, null);
        initView(v);
        AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
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

    private void GetVersion() {
        dialog.show();
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
                        dialog.dismiss();
                    }

                    @Override
                    public void onFinish() {
                        super.onFinish();
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
        if (version <= Integer.parseInt(GetAppVersion.getVersionCode(SettingActivity.this))) {
            Toast.makeText(this, "当前已是最新版本", Toast.LENGTH_SHORT).show();
            return;
        }
        if (version > Integer.parseInt(GetAppVersion.getVersionCode(SettingActivity.this))) {
            AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
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
                    try {
                        Intent intent = new Intent();
                        intent.setAction("android.intent.action.VIEW");
                        Uri content_url = Uri.parse(url);//splitflowurl为分流地址
                        intent.setData(content_url);
                        if (!hasPreferredApplication(SettingActivity.this, intent)) {
                            intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                        }
                        startActivity(intent);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            dialog.show();
        }
    }

    @OnClick({R.id.ll_skin, R.id.tv_update, R.id.tv_pay, R.id.tv_star})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_skin:
                SelectSkinInit();
                break;
            case R.id.tv_update:
                GetVersion();
                break;
            case R.id.tv_pay:
                new AlertDialog.Builder(SettingActivity.this)
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
                    if (!hasPreferredApplication(SettingActivity.this, intent)) {
                        intent.setClassName("com.android.browser", "com.android.browser.BrowserActivity");
                    }
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
