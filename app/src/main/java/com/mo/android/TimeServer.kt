package com.mo.android

import java.text.SimpleDateFormat
import java.util.*

object TimeServer {

    val ONE_DAY_SECOND = 24 * 60 * 60

    val SECOND_MS = 1000

    fun formatYMD(time: Long): String {
        if (time < 0) {
            return ""
        }
        val now = System.currentTimeMillis()
        val dateFormat = SimpleDateFormat()
        if ((now - time) / SECOND_MS > 365 * ONE_DAY_SECOND) {
            dateFormat.applyPattern("yyyy年M月d日")
        } else {
            dateFormat.applyPattern("M月d日")
        }
        return dateFormat.format(Date(time))
    }

    fun formatHMS(time: Long): String {
        if (time < 0) {
            return ""
        }
        val dateFormat = SimpleDateFormat()
        dateFormat.applyPattern("HH∶mm∶ss")

        return dateFormat.format(Date(time))
    }

    fun getTimeDays(time: Long): Long {
        var day = (time / SECOND_MS) / ONE_DAY_SECOND
        if (time > day * ONE_DAY_SECOND * SECOND_MS) {
            day++
        }
        return day
    }

}