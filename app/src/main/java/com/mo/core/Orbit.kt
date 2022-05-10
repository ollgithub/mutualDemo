package com.mo.core

import android.view.animation.PathInterpolator
import com.mo.currentTime
import kotlin.math.min


/**
 * change框架
 * 内容
 * 尺寸
 * 位置
 * alpha
 * shader
 * effect
 *
 *
 */
open class Orbit<T>(
    val doChange: (Float, T?) -> Unit,
    val calculateDuration: (() -> Long)? = null
) {

    companion object {
        val defaultInterpolator = PathInterpolator(0.4f, 0f, 0.8f, 1f)
    }

    var end: T? = null
        private set

    var interpolator = defaultInterpolator
        private set

    var duration = 0L
        private set

    var startTime = 0L
        private set

    var orbitProcess = 0f
        private set

    val runing: Boolean
        get() = duration != 0L

    fun change(end: T, duration: Long = 300) {
        this.end = end
        this.duration = duration
        orbitProcess = 0f
        startTime = currentTime()
        if (duration <= 0L) {
            doChange(startTime)
        } else {
            OrbitCore.start(this)
        }
    }

    fun doChange(currentTime: Long = currentTime()): Boolean {
        if (orbitProcess >= 1f || duration <= 0) {
            doChange(1f, end)
            duration = 0
            return false
        }

        val currentTimeProcess = (currentTime - startTime) / duration.toFloat()
        val currentProcess = min(interpolator.getInterpolation(currentTimeProcess), 1f)
        val progress = min((currentProcess - orbitProcess) / (1f - orbitProcess), 1f)
        doChange(progress, end)
        orbitProcess = currentProcess
        return currentProcess < 1f
    }

}