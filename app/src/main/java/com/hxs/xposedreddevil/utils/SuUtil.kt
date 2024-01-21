package com.hxs.xposedreddevil.utils

import java.io.IOException

object SuUtil {
    private var process: Process? = null

    /**
     * 结束进程,执行操作调用即可
     */
    fun kill(packageName: String) {
        initProcess()
        killProcess(packageName)
        close()
    }

    /**
     * 初始化进程
     */
    private fun initProcess() {
        try {
            if (process == null) {
                process = Runtime.getRuntime().exec("su")
            }
        } catch (e: IOException) {
            println(e)
        }
    }

    /**
     * 结束进程
     */
    private fun killProcess(packageName: String) {
        val out = process!!.outputStream
        val cmd = "am force-stop $packageName \n"
        try {
            out.write(cmd.toByteArray())
            out.flush()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * 关闭输出流
     */
    private fun close() {
        if (process != null) try {
            process!!.outputStream.close()
            process = null
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}