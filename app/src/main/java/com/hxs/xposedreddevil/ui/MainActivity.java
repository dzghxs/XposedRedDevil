package com.hxs.xposedreddevil.ui;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;

import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.utils.MessageEvent;
import com.hxs.xposedreddevil.utils.PushUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.sw_main)
    Switch swMain;
    @BindView(R.id.sw_own)
    Switch swOwn;
    @BindView(R.id.ll_add_no)
    LinearLayout llAddNo;
    @BindView(R.id.sw_sleep)
    Switch swSleep;
    @BindView(R.id.et_sleep)
    EditText etSleep;
    @BindView(R.id.ll_sleep)
    LinearLayout llSleep;
    @BindView(R.id.sw_sound)
    Switch swSound;
    @BindView(R.id.sw_withdraw)
    Switch swWithdraw;
    @BindView(R.id.sw_push)
    Switch swPush;
    @BindView(R.id.sw_money)
    Switch swMoney;
    @BindView(R.id.sw_wechat)
    Switch swWechat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
        SwitchClickInit();
    }

    private void SwitchClickInit() {
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
            if (PropertiesUtils.getValue(RED_FILE, "withdraw", "2").equals("1")) {
                swWithdraw.setChecked(true);
            } else {
                swWithdraw.setChecked(false);
            }
            if (PropertiesUtils.getValue(RED_FILE, "money", "2").equals("1")) {
                swMoney.setChecked(true);
            } else {
                swMoney.setChecked(false);
            }
            if (PropertiesUtils.getValue(RED_FILE, "openwechat", "2").equals("1")) {
                swWechat.setChecked(true);
            } else {
                swWechat.setChecked(false);
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
        swWithdraw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    PropertiesUtils.putValue(RED_FILE, "withdraw", "1");
                } else {
                    PropertiesUtils.putValue(RED_FILE, "withdraw", "2");
                }
            }
        });
        swMoney.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    PropertiesUtils.putValue(RED_FILE, "money", "1");
                } else {
                    PropertiesUtils.putValue(RED_FILE, "money", "2");
                }
            }
        });
        swWechat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    PropertiesUtils.putValue(RED_FILE, "openwechat", "1");
                } else {
                    PropertiesUtils.putValue(RED_FILE, "openwechat", "2");
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

    @OnClick({R.id.ll_add_no})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_add_no:
                startActivity(new Intent(this, SelectFilterActivity.class));
                break;
        }
    }

}
