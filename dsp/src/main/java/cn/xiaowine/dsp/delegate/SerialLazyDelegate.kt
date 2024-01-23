package cn.xiaowine.dsp.delegate

import android.content.SharedPreferences
import cn.xiaowine.dsp.DSP.opt
import cn.xiaowine.dsp.DSP.isXSPf
import cn.xiaowine.dsp.DSP.saveLazy
import cn.xiaowine.dsp.DSP.sharedPreferences
import de.robv.android.xposed.XSharedPreferences
import java.util.concurrent.Executors
import java.util.concurrent.ThreadFactory
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SerialLazyDelegate<T>(private val default: T?) : ReadWriteProperty<Any, T> {


    override fun getValue(thisRef: Any, property: KProperty<*>): T = synchronized(this) {
        val key = property.name
        return sharedPreferences.opt(key, default)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (isXSPf) return
        taskExecutor.execute {
            val key = property.name
            saveLazy(key to value as Any)
        }
    }

    companion object {
        private val taskExecutor = Executors.newSingleThreadExecutor(ThreadFactory {
            val thread = Thread(it)
            thread.name = "SerialLazyDelegate"
            return@ThreadFactory thread
        })
    }

}