package com.mo.android.assist

import java.io.File
import java.io.InputStream
import kotlin.math.max

object CPUServer {

    val cpuCoreCount by lazy {
        File("/sys/devices/system/cpu/").list()?.filter { name ->
            name.matches("cpu([0-9]+)\$".toRegex())
        }?.size ?: 1
    }

    fun getMaxCpuFreq(): Int {
        var maxFreq = 0
        kotlin.runCatching {
            for (i in 0..cpuCoreCount) {
                val args = listOf(
                    "/system/bin/cat",
                    "/sys/devices/system/cpu/cpu$i/cpufreq/cpuinfo_max_freq"
                )
                val cmd = ProcessBuilder(args)
                val input: InputStream = cmd.start().inputStream
                val re = ByteArray(24)
                var result = ""
                while (input.read(re) != -1) {
                    result += String(re)
                }
                input.close()
                result = result.trim { it <= ' ' }
                maxFreq = max(maxFreq, result.toInt())
            }
        }.onFailure {
            it.printStackTrace()
        }
        return maxFreq
    }
}