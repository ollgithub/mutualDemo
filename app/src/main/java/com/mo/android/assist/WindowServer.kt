package com.mo.android.assist

import android.app.Activity
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.view.Surface
import com.mo.base.XY
import kotlin.math.max
import kotlin.math.min

object WindowServer {

    private var windowSize = XY(0, 0)

    val height
        get() = if (isPortrait) windowSize.y else windowSize.x

    val width
        get() = if (!isPortrait) windowSize.y else windowSize.x

    val h_w
        get() = height.toFloat() / width.toFloat()

    var density: Float = 0f
        private set

    var origation: Int = 0
        private set

    val isPortrait: Boolean
        get() = origation == Surface.ROTATION_0 || origation == Surface.ROTATION_180

    fun initialization() {
        val context = CONTEXT
        context.resources.displayMetrics.run {
            windowSize.set(min(widthPixels, heightPixels), max(heightPixels, widthPixels))
            WindowServer.density = density
        }
    }

    fun onConfigurationChanged(newConfig: Configuration? = CONTEXT.resources.configuration) {
        //  e("onConfigurationChanged $newConfig")
        origation = ACTIVITY.windowManager.defaultDisplay.rotation
    }

    fun switchColorMode(activity: Activity) {
        activity.window.colorMode = ActivityInfo.COLOR_MODE_HDR
    }

}