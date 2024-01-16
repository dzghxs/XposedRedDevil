package com.hxs.xposedreddevil.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
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
import com.hxs.xposedreddevil.databinding.ActivityHomeBinding;
import com.hxs.xposedreddevil.utils.AssetsCopyTOSDcard;
import com.hxs.xposedreddevil.utils.MessageEvent;
import com.hxs.xposedreddevil.utils.MultiprocessSharedPreferences;
import com.hxs.xposedreddevil.utils.PackageManagerUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.List;

import static com.hxs.xposedreddevil.utils.Constant.currApkPath;

import androidx.appcompat.app.AlertDialog;

public class HomeActivity extends BaseActivity {

    private ActivityHomeBinding binding;

    Gson gson = new Gson();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHomeBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        EventBus.getDefault().register(this);
        DisplayMetrics outMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
        int widthPixels = outMetrics.widthPixels;
        sharedPreferences.edit().putString("widthPixels", String.valueOf(widthPixels)).commit();
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
        binding.tvHomeUnroot.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font.ttf"));
        binding.tvHomeRoot.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/font.ttf"));
        AssetsCopyTOSDcard.Assets2Sd(this, "lucky_sound.mp3", Environment.getExternalStorageDirectory().toString() + "/xposedreddevil/lucky_sound.mp3");
        if (sharedPreferences.getString("wechatversion", "").equals("8.0.45")) {
            binding.spCenterVersion.setSelection(0);
        }
        if (binding.spCenterVersion.getSelectedItem().equals("8.0.45")) {
            sharedPreferences.edit().putString("wechatversion", "8.0.45").commit();
        }
        if (!PackageManagerUtil.getItems(this).equals("")) {
            sharedPreferences.edit().putString("wechatversion", PackageManagerUtil.getItems(this)).commit();
        } else {
            WriteVersion();
        }
        binding.ivHomeSetting.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, SettingActivity.class)));
        binding.homeCardUnroot.setOnClickListener(v -> {
            Toast.makeText(this, "只支持8.0.32版本，其他版本自行编译代码", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, NotRootActivity.class));
        });
        binding.homeCardRoot.setOnClickListener(v -> startActivity(new Intent(HomeActivity.this, MainActivity.class)));
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

}
