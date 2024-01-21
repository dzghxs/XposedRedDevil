package com.hxs.xposedreddevil.ui

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import com.hxs.xposedreddevil.base.BaseActivity
import com.hxs.xposedreddevil.databinding.ActivityNotRootBinding
import com.hxs.xposedreddevil.utils.PackageManagerUtil

class NotRootActivity : BaseActivity() {
    private var binding: ActivityNotRootBinding? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNotRootBinding.inflate(LayoutInflater.from(this))
        setContentView(binding!!.root)
        DataInit()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    private fun DataInit() {
        try {
            if (config.rednorootmain) {
                if (!PackageManagerUtil.isAccessibilitySettingsOn(this)) {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    startActivity(intent)
                }
                binding!!.swNorootMain.isChecked = true
            } else {
                binding!!.swNorootMain.isChecked = false
            }
            binding!!.swNorootDisturb.isChecked = config.nottootdisturb
            binding!!.swNorootOwn.isChecked = config.notrooown
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding!!.swNorootMain.setOnCheckedChangeListener { buttonView, b ->
            config.rednorootmain = b
            if (b) {
                if (!PackageManagerUtil.isAccessibilitySettingsOn(this@NotRootActivity)) {
                    val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
                    startActivity(intent)
                }
            } else {
            }
        }
        binding!!.swNorootDisturb.setOnCheckedChangeListener { buttonView, b ->
            config.nottootdisturb = b
        }
        binding!!.swNorootOwn.setOnCheckedChangeListener { buttonView, isChecked ->
            config.nottootdisturb = isChecked
        }
        binding!!.tvNorootName.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@NotRootActivity,
                    SetNameActivity::class.java
                )
            )
        }
    }
}
