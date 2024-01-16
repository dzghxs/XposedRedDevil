package com.hxs.xposedreddevil.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.base.BaseActivity;
import com.hxs.xposedreddevil.databinding.ActivityHomeBinding;
import com.hxs.xposedreddevil.databinding.ActivityNotRootBinding;
import com.hxs.xposedreddevil.utils.PackageManagerUtil;

public class NotRootActivity extends BaseActivity {

    private ActivityNotRootBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityNotRootBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        DataInit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private void DataInit() {
        try {
            if (sharedPreferences.getString("rednorootmain", "2").equals("1")) {
                if (!PackageManagerUtil.isAccessibilitySettingsOn(this)) {
                    Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                    startActivity(intent);
                }
                binding.swNorootMain.setChecked(true);
            } else {
                binding.swNorootMain.setChecked(false);
            }
            if (sharedPreferences.getString( "nottootdisturb", "2").equals("1")) {
                binding.swNorootDisturb.setChecked(true);
            } else {
                binding.swNorootDisturb.setChecked(false);
            }
            if (sharedPreferences.getString( "notrooown", "2").equals("1")) {
                binding.swNorootOwn.setChecked(true);
            } else {
                binding.swNorootOwn.setChecked(false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        binding.swNorootMain.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    if (!PackageManagerUtil.isAccessibilitySettingsOn(NotRootActivity.this)) {
                        Intent intent = new Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS);
                        startActivity(intent);
                    }
                    sharedPreferences.edit().putString("rednorootmain", "1").commit();
                } else {
                    sharedPreferences.edit().putString( "rednorootmain", "2").commit();
                }
            }
        });
        binding.swNorootDisturb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean b) {
                if (b) {
                    sharedPreferences.edit().putString( "nottootdisturb", "1").commit();
                } else {
                    sharedPreferences.edit().putString( "nottootdisturb", "2").commit();
                }
            }
        });
        binding.swNorootOwn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    sharedPreferences.edit().putString( "notrooown", "1").commit();
                } else {
                    sharedPreferences.edit().putString( "notrooown", "2").commit();
                }
            }
        });
        binding.tvNorootName.setOnClickListener(v -> startActivity(new Intent(NotRootActivity.this, SetNameActivity.class)));
    }

}
