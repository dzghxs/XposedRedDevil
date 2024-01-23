package com.hxs.xposedreddevil.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import com.hxs.xposedreddevil.base.BaseActivity
import com.hxs.xposedreddevil.databinding.ActivitySetNameBinding

class SetNameActivity : BaseActivity() {
    private var binding: ActivitySetNameBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySetNameBinding.inflate(LayoutInflater.from(this))
        setContentView(binding!!.root)
        binding!!.etName.setText(config.nottootname)
        binding!!.fabName.setOnClickListener { v: View? ->
            if (binding!!.etName.text.toString().isEmpty()) {
                Toast.makeText(this@SetNameActivity, "请输入微信昵称", Toast.LENGTH_SHORT).show()
            } else {
                config.nottootname = binding!!.etName.text.toString().trim { it <= ' ' }
                finish()
            }
        }
    }
}
