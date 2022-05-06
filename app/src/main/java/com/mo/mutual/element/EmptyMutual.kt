package com.mo.mutual.element

import com.mo.base.XY
import com.mo.mutual.Frame
import com.mo.mutual.layout.LayoutScope


fun LayoutScope.Empty(
): EmptyMutual = declare {
    update()
}

class EmptyMutual : ShapeMutual() {


    fun width(width: Int) = apply {

    }

    fun update(
    ) {
    }

    override fun onDraw(frame: Frame) {

    }

    override fun measureLayout(size: XY, fitContent: Boolean) {

    }
}

