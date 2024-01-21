package cn.xiaowine.dsp.delegate

import cn.xiaowine.dsp.DSP.isXSPf
import cn.xiaowine.dsp.DSP.opt
import cn.xiaowine.dsp.DSP.save
import cn.xiaowine.dsp.DSP.sharedPreferences
import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty

class SerialDelegate<T>(private val default: T?) : ReadWriteProperty<Any, T> {


    override fun getValue(thisRef: Any, property: KProperty<*>): T {
        val key = property.name
        return sharedPreferences.opt(key, default)
    }

    override fun setValue(thisRef: Any, property: KProperty<*>, value: T) {
        if (isXSPf) return
        val key = property.name

        save(key to value as Any)


    }

}