package com.mo.mutual.shader

import android.graphics.Bitmap
import android.graphics.BitmapShader
import com.mo.base.Color

class ColorShader(var color: Color) : Shader() {

    override fun getShader(width: Int, height: Int): android.graphics.Shader {
        return BitmapShader(
            Bitmap.createBitmap(1, 1, Bitmap.Config.RGB_565),
            android.graphics.Shader.TileMode.CLAMP, android.graphics.Shader.TileMode.CLAMP
        )
    }
}