package com.hxs.xposedreddevil.ui;

import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.widget.CompoundButton;
import android.widget.Switch;

import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.utils.PackageManagerUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;

public class NotRootActivity extends AppCompatActivity {

    @BindView(R.id.sw_noroot_main)
    Switch swNorootMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_not_root);
        ButterKnife.bind(this);
        DataInit();
    }

    private void DataInit(){
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
    }
}
