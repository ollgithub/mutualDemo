package com.mo.mutual

import android.graphics.*
import com.mo.base.Color
import com.mo.base.SpaceInfo
import com.mo.f
import com.mo.i
import com.mo.mutual.effect.Effect
import com.mo.mutual.effect.Shadow
import com.mo.mutual.shader.Shader

/**
 * mutual的通用绘制能力
 */
class Frame {

    var androidCanvas: Canvas? = null

    val frameImpl: Canvas
        get() = androidCanvas ?: Canvas()

    val currSpaceInfo = SpaceInfo()

    val rootSpaceInfo = SpaceInfo()

    val mainPaint = Paint().apply { isAntiAlias = true }


    fun setShader(shader: Shader?) {
        shader ?: let {
            mainPaint.shader = null
            return
        }
        mainPaint.shader = shader.getShader(currSpaceInfo.w, currSpaceInfo.h)
    }


    fun setEffect(effect: Effect?) {
        (effect as? Shadow)?.run {
            mainPaint.setShadowLayer(radio, dx, dy, color.value)
            return
        }
        mainPaint.clearShadowLayer()
    }

    var debug = false

    val lastSpaceInfo = SpaceInfo()

    inline fun limit(
        newSpaceInfo: SpaceInfo,
        clip: Boolean = false,
        mutual: Mutual? = null,
        function: () -> Unit
    ) {
        lastSpaceInfo.set(currSpaceInfo)
        val n = frameImpl.save()
        currSpaceInfo.set(newSpaceInfo)
        frameImpl.translate(currSpaceInfo.x.f, currSpaceInfo.y.f)
        if (clip) {
            frameImpl.clipRect(0, 0, currSpaceInfo.w, currSpaceInfo.h)
        }
        function()
        if (debug) drawBound(newSpaceInfo)
        frameImpl.restoreToCount(n)
        currSpaceInfo.set(lastSpaceInfo)
    }

    val boundPaint = Paint().apply {
        color = Color.OR.value
        strokeWidth = 1f
        style = Paint.Style.STROKE

    }

    fun drawBound(newSpaceInfo: SpaceInfo) {
        currSpaceInfo.set(newSpaceInfo)
        frameImpl.drawRect(Rect(1, 1, currSpaceInfo.w, currSpaceInfo.h), boundPaint)
    }

    private val src = Rect()
    private val tar = Rect()


    fun drawBitmap(bitmap: Bitmap, alpha: Float) {
        mainPaint.isFilterBitmap = true
        mainPaint.alpha = (alpha * 255).i
        src.set(0, 0, bitmap.width, bitmap.height)
        tar.set(0, 0, currSpaceInfo.w, currSpaceInfo.h)
        frameImpl.drawBitmap(bitmap, src, tar, mainPaint)
    }


    fun drawString(
        string: String,
        size: Float,
        typeface: Typeface? = null
    ) {
        mainPaint.textSize = size
        mainPaint.style = Paint.Style.FILL
        mainPaint.typeface = typeface
        frameImpl.drawText(string, 0f, currSpaceInfo.h.f - mainPaint.descent(), mainPaint)
    }

    fun drawRect() {
        currSpaceInfo.run {
            mainPaint.strokeWidth = 20f
            mainPaint.style = Paint.Style.FILL
            frameImpl.drawRect(0f, 0f, w.f, h.f, mainPaint)
        }
    }

    fun drawCircle() {
        currSpaceInfo.run {
            mainPaint.strokeWidth = 10f
            mainPaint.style = Paint.Style.FILL
            frameImpl.drawOval(0f, 0f, w.f, h.f, mainPaint)
        }
    }

}