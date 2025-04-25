/*
 * StatusBarLyric
 * Copyright (C) 2021-2022 fkj@fkj233.cn
 * https://github.com/577fkj/StatusBarLyric
 *
 * This software is free opensource software: you can redistribute it
 * and/or modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either
 * version 3 of the License, or any later version and our eula as published
 * by 577fkj.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * and eula along with this software.  If not, see
 * <https://www.gnu.org/licenses/>
 * <https://github.com/577fkj/StatusBarLyric/blob/main/LICENSE>.
 */

@file:Suppress("DEPRECATION")

package com.hxs.xposedreddevil.utils.xshare

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import com.hxs.xposedreddevil.BuildConfig
import de.robv.android.xposed.XSharedPreferences
import java.lang.reflect.Field
import java.util.*


@SuppressLint("StaticFieldLeak")
object Utils {
    @JvmStatic
    fun getPref(key: String?): XSharedPreferences? {
        val pref = XSharedPreferences(BuildConfig.APPLICATION_ID, key)
        return if (pref.file.canRead()) pref else null
    }

    @SuppressLint("WorldReadableFiles")
    @Suppress("DEPRECATION")
    @JvmStatic
    fun getSP(context: Context, key: String?): SharedPreferences? {

        // 通过反射获取 MODE_WORLD_READABLE 的值（通常为 1）
        val field: Field = Context::class.java.getField("MODE_WORLD_READABLE")
        val modeWorldReadable: Int = field.getInt(null)
        return context.getSharedPreferences(key, modeWorldReadable)
    }

    fun <E> Array<E>.indexOfArr(value: E): Int {
        for (index in 0..this.size) {
            if (this[index] == value) return index
        }
        return -1
    }

    fun Any?.isNull(callback: () -> Unit) {
        if (this == null) callback()
    }

    fun Any?.isNotNull(callback: () -> Unit) {
        if (this != null) callback()
    }

    fun Any?.isNull() = this == null

    fun Any?.isNotNull() = this != null

}
