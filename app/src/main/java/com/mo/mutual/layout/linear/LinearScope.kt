package com.mo.mutual.layout.linear

import com.mo.mutual.Mutual
import com.mo.mutual.layout.Layout
import com.mo.mutual.layout.LayoutScope
import com.mo.mutual.layout.space.SpacePositionScope

class LinearScope(layout: Layout) : LayoutScope(layout) {

    fun singleRun(layout: Layout, declareFunction: (LinearScope.() -> Unit)?) {
        baseSingleRun(
            layout = layout
        ) {
            declareFunction?.invoke(this)
        }
    }

    fun Mutual.position(
        call: LinearPositionScope.() -> Unit
    ) = apply {
//        bindPositionCall(call)
    }
}