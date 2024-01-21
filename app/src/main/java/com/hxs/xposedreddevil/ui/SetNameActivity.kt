package com.hxs.xposedreddevil.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.hxs.xposedreddevil.R;
import com.hxs.xposedreddevil.base.BaseActivity;
import com.hxs.xposedreddevil.databinding.ActivityHomeBinding;
import com.hxs.xposedreddevil.databinding.ActivitySetNameBinding;

public class SetNameActivity extends BaseActivity {

    private ActivitySetNameBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetNameBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        binding.etName.setText(sharedPreferences.getString("nottootname", ""));
        binding.fabName.setOnClickListener(v -> {
            if (binding.etName.getText().toString().isEmpty()) {
                Toast.makeText(SetNameActivity.this, "请输入微信昵称", Toast.LENGTH_SHORT).show();
            } else {
                sharedPreferences.edit().putString("nottootname", binding.etName.getText().toString().trim()).commit();
                finish();
            }
        });
    }

}
