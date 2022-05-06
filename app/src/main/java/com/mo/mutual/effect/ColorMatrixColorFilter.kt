package com.mo.mutual.effect

import com.mo.base.Color

class ColorMatrixColorFilter(
    val radio: Float = 8f,
    val dx: Float = 0f,
    val dy: Float = 0f,
    val color: Int = Color.BLACK.value,
) : Effect() {
}
