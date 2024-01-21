package com.hxs.xposedreddevil.config

import cn.xiaowine.dsp.delegate.Delegate.serial
import cn.xiaowine.dsp.delegate.Delegate.serialLazy

class SafeConfig {
    var redMain: Boolean by serialLazy(false)
    var red: Boolean by serialLazy(false)
    var privates: Boolean by serialLazy(false)
    var sleep: Boolean by serialLazy(false)
    var sound: Boolean by serialLazy(false)
    var push: Boolean by serialLazy(false)
    var withdraw: Boolean by serialLazy(false)
    var money: Boolean by serialLazy(false)
    var openwechat: Boolean by serialLazy(false)
    var sleeptime: String by serial("1")
    var rednorootmain: Boolean by serialLazy(false)
    var nottootdisturb: Boolean by serialLazy(false)
    var notrooown: Boolean by serialLazy(false)
    var filter: String by serial("")
    var selectfilter: String by serial("")
    var nottootname: String by serial("")
    var redskin: String by serial("亮色")
    var wechatversion: String by serial("未知版本")
}