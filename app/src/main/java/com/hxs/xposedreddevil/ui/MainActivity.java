package com.hxs.xposedreddevil.ui;

import android.content.DialogInterface;
import android.didikee.donate.AlipayDonate;
import android.didikee.donate.WeiXinDonate;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.hjq.permissions.OnPermission;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;
import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;

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

    private final String payCode = "FKX03573LOMYIBUT6ERCF1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        CheckPermissionInit();
        SwitchClickInit();

    }

    private void SwitchClickInit(){
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

}
