package com.mo.mutual.element

import android.graphics.Paint
import com.mo.base.XY
import com.mo.mutual.Mutual

abstract class ShapeMutual : Mutual() {

    internal var beforeDrawCall: ((Paint) -> Unit)? = null

    var currentSize = XY()

    fun beforeDraw(beforeDrawCall: ((Paint) -> Unit)?) = apply {
        this.beforeDrawCall = beforeDrawCall
    }

    override fun measureLayout(size: XY, fitContent: Boolean) {
        size.set(currentSize)
    }
}