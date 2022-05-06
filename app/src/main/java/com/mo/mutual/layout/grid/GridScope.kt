package com.mo.mutual.layout.grid

import com.mo.mutual.Mutual
import com.mo.mutual.layout.Layout
import com.mo.mutual.layout.LayoutScope
import com.mo.mutual.layout.linear.LinearPositionScope

class GridScope(layout: Layout) : LayoutScope(layout) {

    fun singleRun(layout: Layout, declareFunction: (GridScope.() -> Unit)?) {
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