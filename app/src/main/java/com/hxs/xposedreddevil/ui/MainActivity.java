package com.hxs.xposedreddevil.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.base.BaseActivity;
import com.hxs.xposedreddevil.databinding.ActivityHomeBinding;
import com.hxs.xposedreddevil.databinding.ActivityMainBinding;
import com.hxs.xposedreddevil.utils.MessageEvent;
import com.hxs.xposedreddevil.utils.MultiprocessSharedPreferences;
import com.hxs.xposedreddevil.utils.PushUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import androidx.annotation.RequiresApi;

public class MainActivity extends BaseActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        EventBus.getDefault().register(this);
        SwitchClickInit();
    }

    private void SwitchClickInit() {
        try {
            if (sharedPreferences.getString("redmain", "2").equals("1")) {
                binding.swMain.setChecked(true);
            } else {
                binding.swMain.setChecked(false);
            }
            if (sharedPreferences.getString("red", "2").equals("1")) {
                binding.swOwn.setChecked(true);
            } else {
                binding.swOwn.setChecked(false);
            }
            if (sharedPreferences.getString("private", "2").equals("1")) {
                binding.swSlno.setChecked(true);
            } else {
                binding.swSlno.setChecked(false);
            }
            if (sharedPreferences.getString("sleep", "2").equals("1")) {
                binding.swSleep.setChecked(true);
                binding.llSleep.setVisibility(View.VISIBLE);
            } else {
                binding.swSleep.setChecked(false);
                binding.llSleep.setVisibility(View.GONE);
            }
            if (sharedPreferences.getString("sound", "2").equals("1")) {
                binding.swSound.setChecked(true);
            } else {
                binding.swSound.setChecked(false);
            }
            if (sharedPreferences.getString("push", "2").equals("1")) {
                binding.swPush.setChecked(true);
            } else {
                binding.swPush.setChecked(false);
            }
            if (sharedPreferences.getString("withdraw", "2").equals("1")) {
                binding.swWithdraw.setChecked(true);
            } else {
                binding.swWithdraw.setChecked(false);
            }
            if (sharedPreferences.getString("money", "2").equals("1")) {
                binding.swMoney.setChecked(true);
            } else {
                binding.swMoney.setChecked(false);
            }
            if (sharedPreferences.getString("openwechat", "2").equals("1")) {
                binding.swWechat.setChecked(true);
            } else {
                binding.swWechat.setChecked(false);
            }
            binding.etSleep.setText(sharedPreferences.getString("sleeptime", "1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.swMain.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                sharedPreferences.edit().putString("redmain", "1").commit();
            } else {
                sharedPreferences.edit().putString("redmain", "2").commit();
            }
        });
        binding.swOwn.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                sharedPreferences.edit().putString("red", "1").commit();
            } else {
                sharedPreferences.edit().putString("red", "2").commit();
            }
        });
        //TODO 不抢私聊红包
        binding.swSlno.setOnCheckedChangeListener((compoundButton, b) -> {
            binding.swSlno.setChecked(false);
            Toast.makeText(this, "还在开发中~", Toast.LENGTH_SHORT).show();
            if (b) {
                sharedPreferences.edit().putString("private", "1").commit();
            } else {
                sharedPreferences.edit().putString("private", "2").commit();
            }
        });
        binding.swSleep.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                sharedPreferences.edit().putString("sleep", "1").commit();
                binding.llSleep.setVisibility(View.VISIBLE);
            } else {
                sharedPreferences.edit().putString("sleep", "2").commit();
                binding.llSleep.setVisibility(View.GONE);
            }
        });
        binding.swSound.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                sharedPreferences.edit().putString("sound", "1").commit();
            } else {
                sharedPreferences.edit().putString("sound", "2").commit();
            }
        });
        binding.swPush.setOnCheckedChangeListener((compoundButton, b) -> {
            if (b) {
                sharedPreferences.edit().putString("push", "1").commit();
            } else {
                sharedPreferences.edit().putString("push", "2").commit();
            }
        });
        binding.swWithdraw.setOnCheckedChangeListener((buttonView, b) -> {
            if (b) {
                sharedPreferences.edit().putString("withdraw", "1").commit();
            } else {
                sharedPreferences.edit().putString("withdraw", "2").commit();
            }
        });
        binding.swMoney.setOnCheckedChangeListener((buttonView, b) -> {
            if (b) {
                sharedPreferences.edit().putString("money", "1").commit();
            } else {
                sharedPreferences.edit().putString("money", "2").commit();
            }
        });
        binding.swWechat.setOnCheckedChangeListener((buttonView, b) -> {
            if (b) {
                sharedPreferences.edit().putString("openwechat", "1").commit();
            } else {
                sharedPreferences.edit().putString("openwechat", "2").commit();
            }
        });
        binding.etSleep.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().equals("0")) {
                    sharedPreferences.edit().putString("sleeptime", "1").commit();
                } else {
                    sharedPreferences.edit().putString("sleeptime", editable.toString()).commit();
                }
            }
        });
        binding.llAddNo.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, SelectFilterActivity.class)));
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

}
