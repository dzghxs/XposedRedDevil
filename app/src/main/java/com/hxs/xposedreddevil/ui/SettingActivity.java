package com.hxs.xposedreddevil.ui;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;
import com.hxs.xposedreddevil.weight.LoadingDialog;

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

    private LinearLayout llLight;
    private RadioButton rbLight;
    private View vLight;
    private LinearLayout llNight;
    private RadioButton rbNight;
    private View vNight;
    private TextView tvDismiss;

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

    @OnClick({R.id.ll_skin})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_skin:
                SelectSkinInit();
                break;
        }
    }
}
