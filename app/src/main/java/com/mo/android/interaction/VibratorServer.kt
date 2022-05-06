package com.mo.android.interaction

import android.app.Service
import android.os.VibrationEffect
import android.os.Vibrator
import com.mo.android.assist.CONTEXT

object VibratorServer {


    val vb = CONTEXT.getSystemService(Service.VIBRATOR_SERVICE) as Vibrator

    fun shot() {
        vb.vibrate(VibrationEffect.createOneShot(50L, 255))
    }

}