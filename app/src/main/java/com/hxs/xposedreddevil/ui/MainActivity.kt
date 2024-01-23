package com.hxs.xposedreddevil.ui

import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.CompoundButton
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import com.hxs.xposedreddevil.base.BaseActivity
import com.hxs.xposedreddevil.databinding.ActivityMainBinding
import com.hxs.xposedreddevil.utils.MessageEvent
import com.hxs.xposedreddevil.utils.PushUtils
import com.hxs.xposedreddevil.utils.SuUtil
import com.hxs.xposedreddevil.utils.xshare.Utils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import kotlin.system.exitProcess

class MainActivity : BaseActivity() {
    private var binding: ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))
        setContentView(binding!!.root)
        EventBus.getDefault().register(this)
        SwitchClickInit()
    }

    private fun SwitchClickInit() {
        try {
            binding!!.swMain.isChecked = config.redMain
            binding!!.swOwn.isChecked = config.red
            binding!!.swSlno.isChecked = config.privates
            if (config.sleep) {
                binding!!.swSleep.isChecked = true
                binding!!.llSleep.visibility = View.VISIBLE
            } else {
                binding!!.swSleep.isChecked = false
                binding!!.llSleep.visibility = View.GONE
            }
            binding!!.swSound.isChecked = config.sound
            binding!!.swPush.isChecked = config.push
            binding!!.swWithdraw.isChecked = config.withdraw
            binding!!.swMoney.isChecked = config.money
            binding!!.swWechat.isChecked = config.openwechat
            binding!!.etSleep.setText(config.sleeptime)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        binding!!.swMain.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            config.redMain = b
            SuUtil.kill("com.tencent.mm")
        }
        binding!!.swOwn.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            config.red = b
            SuUtil.kill("com.tencent.mm")
        }
        //TODO 不抢私聊红包
        binding!!.swSlno.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            binding!!.swSlno.isChecked = false
            Toast.makeText(this, "还在开发中~", Toast.LENGTH_SHORT).show()
            config.privates = b
        }
        binding!!.swSleep.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            config.sleep = b
            if (b) {
                binding!!.llSleep.visibility = View.VISIBLE
            } else {
                binding!!.llSleep.visibility = View.GONE
            }
            SuUtil.kill("com.tencent.mm")
        }
        binding!!.swSound.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            config.sound = b
            SuUtil.kill("com.tencent.mm")
        }
        binding!!.swPush.setOnCheckedChangeListener { compoundButton: CompoundButton?, b: Boolean ->
            config.push = b
            SuUtil.kill("com.tencent.mm")
        }
        binding!!.swWithdraw.setOnCheckedChangeListener { buttonView: CompoundButton?, b: Boolean ->
            config.withdraw = b
            SuUtil.kill("com.tencent.mm")
        }
        binding!!.swMoney.setOnCheckedChangeListener { buttonView: CompoundButton?, b: Boolean ->
            config.money = b
            SuUtil.kill("com.tencent.mm")
        }
        binding!!.swWechat.setOnCheckedChangeListener { buttonView: CompoundButton?, b: Boolean ->
            config.openwechat = b
            SuUtil.kill("com.tencent.mm")
        }
        binding!!.etSleep.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun onTextChanged(charSequence: CharSequence, i: Int, i1: Int, i2: Int) {}
            override fun afterTextChanged(editable: Editable) {
                config.sleeptime = if (editable.toString() == "0") "1" else editable.toString()
            }
        })
        binding!!.llAddNo.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@MainActivity,
                    SelectFilterActivity::class.java
                )
            )
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getMsg(messages: MessageEvent?) {
        PushUtils.showNotification(this, "private", "25", "微信", "天降红包")
    }

    override fun onRestart() {
        super.onRestart()
    }
}
