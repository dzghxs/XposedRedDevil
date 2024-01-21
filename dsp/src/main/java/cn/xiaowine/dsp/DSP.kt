@file:Suppress("DEPRECATION")

package cn.xiaowine.dsp


import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import cn.xiaowine.dsp.data.MODE
import de.robv.android.xposed.XSharedPreferences

@SuppressLint("StaticFieldLeak")
object DSP {

    lateinit var sharedPreferences: SharedPreferences
    var isXSPf: Boolean = false


    @SuppressLint("WorldReadableFiles")
    fun init(context: Context?, packageName: String, mode: MODE = MODE.APP, isXSPf: Boolean = false): Boolean {
        this.isXSPf = isXSPf
        return try {
            if (isXSPf) {
                val pref = XSharedPreferences(packageName, packageName)
                if (pref.file.canRead()) {
                    sharedPreferences = pref
                } else {
                    if (!pref.file.exists()) {
                        println("XSharedPreferences can't read")
                    } else {
                        println("XSharedPreferences not exists")
                    }
                }
            } else {
                if (context == null) println("context is null")
                sharedPreferences = if (mode == MODE.HOOK) {
                    context!!.createDeviceProtectedStorageContext().getSharedPreferences(packageName, Context.MODE_WORLD_READABLE)
                } else {
                    context!!.getSharedPreferences(packageName, Context.MODE_PRIVATE)
                }
            }
            true
        } catch (e: Exception) {
            println(e)
            e.printStackTrace()
            false
        }
    }


    @SuppressLint("ApplySharedPref")
    fun save(pairs: Pair<String, Any>) {
        sharedPreferences.edit().apply {
            put(pairs.first, pairs.second)
            commit()
        }
    }

    fun saveLazy(pairs: Pair<String, Any>) {
        sharedPreferences.edit().apply {
            put(pairs.first, pairs.second)
            apply()
        }
    }

    private fun SharedPreferences.Editor.put(key: String, value: Any) {
        when (value) {
            is Int -> putInt(key, value)
            is Long -> putLong(key, value)
            is String -> putString(key, value)
            is Boolean -> putBoolean(key, value)
            is Float -> putFloat(key, value)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> SharedPreferences.opt(key: String, defValue: T?): T {
        return when (defValue) {
            is String -> getString(key, defValue.toString()) as T
            is Long -> getLong(key, defValue.toLong()) as T
            is Int -> getInt(key, defValue) as T
            is Boolean -> getBoolean(key, defValue) as T
            is Float -> getFloat(key, defValue) as T
            else -> defValue ?: throw IllegalArgumentException("defValue is null")
        }
    }
}