package com.mo.mutual.ability

import com.mo.mutual.Mutual
import com.mo.mutual.SizeScope

interface ISizeRule {

    var sizeCall: (SizeScope.() -> Unit)?

    var relyLayout: Boolean

    fun size(
        relyLayout: Boolean = false,
        sizeCall: SizeScope.() -> Unit
    ): Mutual

}