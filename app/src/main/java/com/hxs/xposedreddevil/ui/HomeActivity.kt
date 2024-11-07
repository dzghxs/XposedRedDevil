package com.hxs.xposedreddevil.ui

import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Typeface
import android.os.Bundle
import android.os.Environment
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.Permission
import com.hjq.permissions.XXPermissions
import com.hxs.xposedreddevil.base.BaseActivity
import com.hxs.xposedreddevil.databinding.ActivityHomeBinding
import com.hxs.xposedreddevil.utils.AssetsCopyTOSDcard
import com.hxs.xposedreddevil.utils.Constant
import com.hxs.xposedreddevil.utils.MessageEvent
import com.hxs.xposedreddevil.utils.PackageManagerUtil
import com.hxs.xposedreddevil.utils.xshare.Utils
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.File
import kotlin.system.exitProcess

class HomeActivity : BaseActivity() {
    private var binding: ActivityHomeBinding? = null

    fun setSP(sharedPreferences: SharedPreferences) {
        safeSP.mSP = sharedPreferences
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (checkLSPosed()) {
            binding = ActivityHomeBinding.inflate(LayoutInflater.from(this))
            setContentView(binding!!.root)
            EventBus.getDefault().register(this)
            val outMetrics = DisplayMetrics()
            windowManager.defaultDisplay.getMetrics(outMetrics)
            CheckPermissionInit()
            DataInit()
        }
    }

    private fun checkLSPosed(): Boolean {
        return try {
            Utils.getSP(this, "cloudlocation")?.let { setSP(it) }
            true
        } catch (e: Throwable) {
            AlertDialog.Builder(this)
                .setTitle("本模块未激活，请先激活")
                .setPositiveButton(
                    "关闭应用"
                ) { dialog, which -> exitProcess(0) }.show()
            false
        }
    }

    //d
    override fun onDestroy() {
        EventBus.getDefault().unregister(this)
        super.onDestroy()
    }

    private fun DataInit() {
        val file = File(Constant.currApkPath + "ListTime")
        //判断文件夹是否存在,如果不存在则创建文件夹
        if (!file.exists()) {
            file.mkdir()
        }
        binding!!.tvHomeUnroot.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        binding!!.tvHomeRoot.typeface = Typeface.createFromAsset(assets, "fonts/font.ttf")
        AssetsCopyTOSDcard.Assets2Sd(
            this,
            "lucky_sound.mp3",
            Environment.getExternalStorageDirectory().toString() + "/xposedreddevil/lucky_sound.mp3"
        )
        if (config.wechatversion == "8.0.53") {
            binding!!.spCenterVersion.setSelection(0);
        }
        if (binding!!.spCenterVersion.selectedItem.equals("8.0.53")) {
            config.wechatversion =  "8.0.53"
        }
        if (!PackageManagerUtil.getItems(this).equals("")) {
            config.wechatversion = PackageManagerUtil.getItems(this)
        } else {
            WriteVersion();
        }
        binding!!.ivHomeSetting.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@HomeActivity,
                    SettingActivity::class.java
                )
            )
        }
        binding!!.homeCardUnroot.setOnClickListener { v: View? ->
            Toast.makeText(this, "停止支持免root", Toast.LENGTH_SHORT).show()
//            startActivity(Intent(this, NotRootActivity::class.java))
        }
        binding!!.homeCardRoot.setOnClickListener { v: View? ->
            startActivity(
                Intent(
                    this@HomeActivity,
                    MainActivity::class.java
                )
            )
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun getMsg(msg: MessageEvent?) {
    }

    /**
     * 验证权限
     */
    fun CheckPermissionInit() {
        XXPermissions.with(this)
            .permission(*Permission.Group.STORAGE)
            .permission(Permission.READ_PHONE_STATE)
            .request(object : OnPermissionCallback {
                override fun onGranted(permissions: List<String>, all: Boolean) {}
                override fun onDenied(permissions: List<String>, never: Boolean) {
                    Toast.makeText(
                        this@HomeActivity,
                        "必要权限未提供，app即将关闭",
                        Toast.LENGTH_SHORT
                    ).show()
                    try {
                        Thread.sleep(2000)
                    } catch (e: InterruptedException) {
                        e.printStackTrace()
                    }
                    finish()
                }
            })
    }

    private fun WriteVersion() {
        AlertDialog.Builder(this).setTitle("获取微信版本失败，请手动选择微信版本")
            .setPositiveButton("确定") { dialog: DialogInterface, which: Int -> dialog.dismiss() }
    }
}
