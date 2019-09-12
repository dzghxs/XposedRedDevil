package com.hxs.xposedreddevil.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.base.BaseActivity;
import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.utils.PackageManagerUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;

public class NotRootActivity extends BaseActivity {

    @BindView(R.id.sw_noroot_main)
    Switch swNorootMain;
    @BindView(R.id.sw_noroot_disturb)
    Switch swNorootDisturb;
    @BindView(R.id.sw_noroot_keyword)
    Switch swNorootKeyword;
    @BindView(R.id.tv_noroot_name)
    TextView tvNorootName;
    @BindView(R.id.sw_noroot_own)
    Switch swNorootOwn;
    @BindView(R.id.sw_noroot_list)
    Switch swNorootList;
    @BindView(R.id.tv_noroot_carrylist)
    TextView tvNorootCarrylist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_root);
        ButterKnife.bind(this);
        DataInit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void DataInit() {
        try {
            if (PropertiesUtils.getValue(RED_FILE, "rednorootmain", "2").equals("1")) {
//                if (!PackageManagerUtil.isAccessibilitySettingsOn(this)) {
//                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
//                    startActivity(intent);
//                }
                swNorootMain.setChecked(true);
            } else {
                swNorootMain.setChecked(false);
            }
            if (PropertiesUtils.getValue(RED_FILE, "nottootdisturb", "2").equals("1")) {
                swNorootDisturb.setChecked(true);
            } else {
                swNorootDisturb.setChecked(false);
            }
            if (PropertiesUtils.getValue(RED_FILE, "notrooown", "2").equals("1")) {
                swNorootOwn.setChecked(true);
            } else {
                swNorootOwn.setChecked(false);
            }
            if (PropertiesUtils.getValue(RED_FILE, "notrootlist", "2").equals("1")) {
                swNorootList.setChecked(true);
                tvNorootCarrylist.setVisibility(View.VISIBLE);
            } else {
                swNorootList.setChecked(false);
                tvNorootCarrylist.setVisibility(View.GONE);
            }
            if (PropertiesUtils.getValue(RED_FILE, "nottootkeyword", "2").equals("1")) {
                swNorootKeyword.setChecked(true);
                tvNorootName.setVisibility(View.VISIBLE);
            } else {
                swNorootKeyword.setChecked(false);
                tvNorootName.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        swNorootMain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    if (!PackageManagerUtil.isAccessibilitySettingsOn(NotRootActivity.this)) {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    }
                    PropertiesUtils.putValue(RED_FILE, "rednorootmain", "1");
                } else {
                    PropertiesUtils.putValue(RED_FILE, "rednorootmain", "2");
                }
            }
        });
        swNorootDisturb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    PropertiesUtils.putValue(RED_FILE, "nottootdisturb", "1");
                } else {
                    PropertiesUtils.putValue(RED_FILE, "nottootdisturb", "2");
                }
            }
        });
        swNorootKeyword.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    PropertiesUtils.putValue(RED_FILE, "nottootkeyword", "1");
                    tvNorootName.setVisibility(View.VISIBLE);
                } else {
                    PropertiesUtils.putValue(RED_FILE, "nottootkeyword", "2");
                    tvNorootName.setVisibility(View.GONE);
                }
            }
        });
        swNorootOwn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PropertiesUtils.putValue(RED_FILE, "notrooown", "1");
                } else {
                    PropertiesUtils.putValue(RED_FILE, "notrooown", "2");
                }
            }
        });
        swNorootList.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    PropertiesUtils.putValue(RED_FILE, "notrootlist", "1");
                } else {
                    PropertiesUtils.putValue(RED_FILE, "notrootlist", "2");
                    tvNorootCarrylist.setVisibility(View.GONE);
                }
            }
        });
    }

    @OnClick({R.id.tv_noroot_name, R.id.tv_noroot_carrylist})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_noroot_name:
                startActivity(new Intent(this, SetNameActivity.class));
                break;
            case R.id.tv_noroot_carrylist:
                startActivity(new Intent(this, CarryListActivity.class));
                break;
        }
    }

}
