package com.mo.android

import android.os.Environment
import java.io.BufferedWriter
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStreamWriter
import java.text.DecimalFormat

object FileServer {

    val rootPath = "/storage/emulated/0/"

    val storageMounted: Boolean
        get() = Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED

    var FILE_LENGTH_UNITS = arrayOf("B", "K", "M", "G", "T", "P")

    fun formatFileSize(length: Long, keepDecimalPlaces: Int = 2): String {
        var unit = FILE_LENGTH_UNITS[0]
        var value = length.toFloat()
        for (fileUnit in FILE_LENGTH_UNITS) {
            unit = fileUnit
            if (value < 1024f) {
                break
            }
            value /= 1024
        }
        var pattern = "0."
        for (i in 0 until keepDecimalPlaces) {
            pattern += "#"
        }
        return DecimalFormat(pattern).format(value.toDouble()) + unit
    }

    fun delete(path: String): Boolean {
        val f = File(path)
        if (!f.exists()) {
            return false
        }
        return f.delete()
    }


}