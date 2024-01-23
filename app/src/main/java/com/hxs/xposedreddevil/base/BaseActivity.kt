package com.hxs.xposedreddevil.base

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.xiaowine.dsp.DSP
import cn.xiaowine.dsp.data.MODE
import com.hxs.xposedreddevil.BuildConfig
import com.hxs.xposedreddevil.config.SafeConfig
import com.hxs.xposedreddevil.utils.xshare.SafeSharedPreferences

/**
 * Created by fujiayi on 2017/9/13.
 *
 *
 * 此类 底层UI实现 无SDK相关逻辑
 */
open class BaseActivity : AppCompatActivity() {
    lateinit var config: SafeConfig

    companion object {
        var safeSP: SafeSharedPreferences = SafeSharedPreferences()
    }

    /*
     * @param savedInstanceState
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initialShare() // 初始化UI
    }

    private fun initialShare() {
        DSP.init(this, BuildConfig.APPLICATION_ID, MODE.HOOK)
        config = SafeConfig()
    }
}
