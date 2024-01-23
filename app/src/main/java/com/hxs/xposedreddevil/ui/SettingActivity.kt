package com.hxs.xposedreddevil.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.TextView
import com.hxs.xposedreddevil.R
import com.hxs.xposedreddevil.base.BaseActivity
import com.hxs.xposedreddevil.databinding.ActivitySettingBinding
import com.hxs.xposedreddevil.weight.LoadingDialog

class SettingActivity : BaseActivity() {
    private var binding: ActivitySettingBinding? = null
    private var llLight: LinearLayout? = null
    private var rbLight: RadioButton? = null
    private var vLight: View? = null
    private var llNight: LinearLayout? = null
    private var rbNight: RadioButton? = null
    private var vNight: View? = null
    private var tvDismiss: TextView? = null
    var dialog: LoadingDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingBinding.inflate(LayoutInflater.from(this))
        setContentView(binding!!.root)
        dialog = LoadingDialog(this)
        binding!!.tvSkin.text = config.redskin
    }

    /**
     * 选择主题
     */
    //    private void SelectSkinInit() {
    //        View v = LayoutInflater.from(this).inflate(R.layout.dialog_skin_layout, null);
    //        initView(v);
    //        AlertDialog.Builder dialog = new AlertDialog.Builder(SettingActivity.this);
    //        dialog.setView(v);
    //        final AlertDialog d = dialog.create();
    //        llLight.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //                // 恢复应用默认皮肤
    //                SkinCompatManager.getInstance().restoreDefaultTheme();
    //                PropertiesUtils.putValue(RED_FILE, "redskin", "亮色");
    //                rbLight.setChecked(true);
    //                rbNight.setChecked(false);
    //                tvSkin.setText("亮色");
    //                d.dismiss();
    //            }
    //        });
    //        vLight.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //            }
    //        });
    //        llNight.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //                SkinCompatManager.getInstance().loadSkin("night.skin", SkinCompatManager.SKIN_LOADER_STRATEGY_ASSETS);
    //                PropertiesUtils.putValue(RED_FILE, "redskin", "暗色");
    //                rbLight.setChecked(false);
    //                rbNight.setChecked(true);
    //                tvSkin.setText("暗色");
    //                d.dismiss();
    //            }
    //        });
    //        vNight.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //            }
    //        });
    //        tvDismiss.setOnClickListener(new View.OnClickListener() {
    //            @Override
    //            public void onClick(View v) {
    //                d.dismiss();
    //            }
    //        });
    //        d.show();
    //    }
    private fun initView(v: View) {
        llLight = v.findViewById(R.id.ll_light)
        rbLight = v.findViewById(R.id.rb_light)
        vLight = v.findViewById(R.id.v_light)
        llNight = v.findViewById(R.id.ll_night)
        rbNight = v.findViewById(R.id.rb_night)
        vNight = v.findViewById(R.id.v_night)
        tvDismiss = v.findViewById(R.id.tv_dismiss)
        rbLight!!.isFocusable = false
        rbNight!!.isFocusable = false
        if (config.redskin == "亮色") {
            rbLight!!.isChecked = true
            rbNight!!.isChecked = false
        } else {
            rbLight!!.isChecked = false
            rbNight!!.isChecked = true
        }
    }
}
