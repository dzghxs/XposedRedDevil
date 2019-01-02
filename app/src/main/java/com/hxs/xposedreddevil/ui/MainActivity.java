package com.hxs.xposedreddevil.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.MainThread;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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

public class MainActivity extends AppCompatActivity {

    public static final String RED_FILE = "/storage/emulated/0/xposedreddevil/" + "reddevil_config.pro";

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

    Gson gson = new Gson();

    private final String payCode = "FKX03573LOMYIBUT6ERCF1";

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
        AssetsCopyTOSDcard.Assets2Sd(this,"lucky_sound.mp3",Environment.getExternalStorageDirectory().toString() + "/xposedreddevil/lucky_sound.mp3");
        try {
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
    public void getMsg(MessageEvent messages){
        PushUtils.showNotification(this,"private", "25", "微信", "天降红包");
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

    @OnClick(R.id.tv_pay)
    public void onViewClicked() {
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
    }

    /**
     * 验证权限
     */
    public void CheckPermissionInit() {
        XXPermissions.with(this)
                .permission(Permission.Group.STORAGE)
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

    private void GetVersion() {
        OkGo.<String>post("http://39.105.26.114:9672/redselectRedCode")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        VersionBean versionBean = new VersionBean();
                        if (response.body().contains("200")) {
                            versionBean = gson.fromJson(response.body(), VersionBean.class);
                            UpdataInit(versionBean.getData().getVersionCode(),
                                    versionBean.getData().getUpdateContent(),
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
            dialog.setMessage(updatamsg);
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
}
