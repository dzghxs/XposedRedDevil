package cn.xiaowine.dsp.delegate

import kotlin.properties.ReadWriteProperty

object Delegate {
    inline fun <reified T> serial(default: T? = null): ReadWriteProperty<Any, T> = SerialDelegate(default)


    inline fun <reified T> serialLazy(default: T? = null): ReadWriteProperty<Any, T> = SerialLazyDelegate(default)

}