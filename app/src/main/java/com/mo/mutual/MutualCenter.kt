package com.mo.mutual

import com.mo.*
import com.mo.mutual.layout.IMutualBase
import com.mo.mutual.layout.Layout

object MutualCenter {

    var mutualBase: IMutualBase? = null

    var beLayoutingMutual: Mutual? = null
        private set
    var declaringLayout: Layout? = null
        private set

    val isLayouting: Boolean
        get() = beLayoutingMutual.isNotNull()

    val isDeclaring: Boolean
        get() = declaringLayout.isNotNull()

    var orbit = true

    fun layouting(
        mutual: Mutual,
        function: () -> Unit
    ) {
        val outLayoutingMutual = beLayoutingMutual
        beLayoutingMutual = mutual
        function()
        beLayoutingMutual = outLayoutingMutual
    }

    fun declaring(
        layout: Layout,
        function: () -> Unit
    ) {
        val start = currentTime()
        val outDeclaringLayout = declaringLayout
        declaringLayout = layout
        function()
        declaringLayout = outDeclaringLayout
        d("timee declaring time = ${consuming(start)}")
    }

    fun bindValue(list: ArrayList<() -> Unit>) {
        declaringLayout?.let {
            list.add(it::redeclare)
        }
        beLayoutingMutual?.let {
            list.add(it::relayout)
        }
    }

    fun <T> cacheValue(
        key: Int, value: T,
        withAnim: Boolean = true
    ): MutualSync<T> {
        return declaringLayout!!.cacheValue(key, value, withAnim)
    }


    var flag = 0

    /**
     * 单次重布局
     */
    fun relayout() {
//        if (flag.flagHas(Mutual.RELAYOUT_SHL)) return
        flag = flag.flag(Mutual.RELAYOUT_SHL)
        mutualBase?.relayout()
    }

    fun reDraw() {
        mutualBase?.reDraw()
    }


}