package com.mo.base

data class XY(
    var x: Int = 0,
    var y: Int = 0,
) {

    val isZero: Boolean
        get() = x == 0 && y == 0

    fun set(
        x: Int,
        y: Int
    ): XY = apply {
        this.x = x
        this.y = y
    }

    fun set(
        xy: XY
    ): XY = apply {
        this.x = xy.x
        this.y = xy.y
    }

    override fun toString(): String {
        return "XY($x , $y)"
    }

    override fun hashCode(): Int {
        return x * 31 + y
    }

    override fun equals(
        other: Any?
    ): Boolean {
        return if (other is XY) {
            other.x == x && other.y == y
        } else false
    }

}