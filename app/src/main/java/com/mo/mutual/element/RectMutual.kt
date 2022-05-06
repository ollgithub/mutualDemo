package com.mo.mutual.element

import com.mo.base.XY
import com.mo.mutual.Frame
import com.mo.mutual.layout.LayoutScope


fun LayoutScope.Rect(
): RectMutual = declare {
    update()
}

class RectMutual : ShapeMutual() {


    fun width(width: Int) = apply {

    }

    fun update(
    ) {
    }

    override fun onDraw(frame: Frame) {
        frame.drawRect()
    }

    override fun measureLayout(size: XY, fitContent: Boolean) {

    }
}

