package com.hxs.xposedreddevil.ui;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.contentprovider.PropertiesUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.hxs.xposedreddevil.utils.Constant.RED_FILE;

public class SetNameActivity extends AppCompatActivity {

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.fab_name)
    FloatingActionButton fabName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_name);
        ButterKnife.bind(this);
        etName.setText(PropertiesUtils.getValue(RED_FILE, "nottootname", ""));
    }

    @OnClick(R.id.fab_name)
    public void onViewClicked() {
        if(etName.getText().toString().isEmpty()){
            Toast.makeText(this, "请输入微信昵称", Toast.LENGTH_SHORT).show();
        }else{
            PropertiesUtils.putValue(RED_FILE, "nottootname", etName.getText().toString().trim());
            finish();
        }
    }
}
