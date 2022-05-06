package com.mo.mutual.element

import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RenderNode
import android.os.Build
import android.view.MotionEvent
import androidx.annotation.RequiresApi
import com.mo.base.SpaceInfo
import com.mo.base.XY
import com.mo.d
import com.mo.i
import com.mo.mutual.Frame
import com.mo.mutual.layout.LayoutScope
import com.mo.packHashCode
import com.mo.v


fun LayoutScope.Bitmap(
    bitmap: Bitmap? = null,
    alpha: Boolean = false
): BitmapMutual = declare {
    val change = contentChange(
        bitmap,
        alpha
    )

}

class BitmapMutual : ShapeMutual() {

    var bitmap: Bitmap? = null

    var transParent: Boolean = false

    fun contentChange(
        bitmap: Bitmap? = null,
        transParent: Boolean = false
    ): Boolean {

        val change = (this.bitmap != bitmap) || (this.transParent != transParent)
        this.bitmap = bitmap
        this.transParent = transParent
        if (change) {
            relayout()
        }
        return change
    }

    override fun onDraw(frame: Frame) {
        val bitmap = bitmap ?: return
        frame.drawBitmap(bitmap, alpha)
    }

    override fun measureLayout(size: XY, fitContent: Boolean) {
        size.set(bitmap?.width ?: 0, bitmap?.height ?: 0)
    }

    override fun input(event: MotionEvent): Boolean {
        return super.input(event)
    }

    fun orbit(call: BitmapMutual.() -> Unit) = apply {

    }

}