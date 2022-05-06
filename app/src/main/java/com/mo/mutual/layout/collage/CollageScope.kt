package com.mo.mutual.layout.collage

import com.mo.index
import com.mo.mutual.Mutual
import com.mo.mutual.layout.Layout
import com.mo.mutual.layout.LayoutScope

class CollageScope(layout: Layout) : LayoutScope(layout) {

    val positionScope by lazy {
        CollagePositionScope()
    }

    fun Mutual.position(
        call: CollagePositionScope.() -> Unit
    ) = apply {
//        val last = baseContents[declareCursor.index]
//        if (last.mutual == this) {
//
//        } else {
//            baseContents.find { it.mutual == this }?.let{
//                doo.invoke(it)
//            }
//        }
    }

    fun <T> singleRun(
        item: T? = null,
        layout: Layout,
        declareFunction: (CollageScope.(T) -> Unit)?
    ) {
        baseSingleRun(
            end = false,
            layout = layout
        ) {
            declareFunction?.invoke(this, item!!)
        }
    }

}