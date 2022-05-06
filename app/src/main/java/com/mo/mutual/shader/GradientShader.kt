package com.mo.mutual.shader

import android.graphics.LinearGradient
import android.graphics.PointF
import android.graphics.RadialGradient
import android.graphics.SweepGradient
import com.mo.base.Color
import kotlin.math.pow
import kotlin.math.sqrt


class GradientShader(
    var type: GradientType,
    var startPoint: PointF,
    var endPoint: PointF,
    var startColor: Color,
    var endColor: Color,
) : Shader() {

    enum class GradientType {
        LINEAR, RADIAL, SWEEP
    }

    override fun getShader(width: Int, height: Int): android.graphics.Shader {
        return when (type) {
            GradientType.LINEAR ->
                LinearGradient(
                    width * startPoint.x,
                    height * startPoint.y,
                    width * endPoint.x,
                    height * endPoint.y,
                    startColor.value,
                    endColor.value,
                    android.graphics.Shader.TileMode.CLAMP
                )
            GradientType.RADIAL -> {
                val centerX = width * startPoint.x
                val centerY = height * startPoint.y
                val endX = width * endPoint.x
                val endY = height * endPoint.y
                RadialGradient(
                    centerX,
                    centerY,
                    sqrt((endX - centerX).pow(2) + (endY - centerY).pow(2)),
                    startColor.value,
                    endColor.value,
                    android.graphics.Shader.TileMode.CLAMP
                )
            }
            GradientType.SWEEP -> {
                SweepGradient(
                    width * startPoint.x,
                    height * startPoint.y,
                    startColor.value,
                    endColor.value,
                )
            }
        }


    }


}