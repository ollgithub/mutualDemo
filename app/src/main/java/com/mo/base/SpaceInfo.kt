package com.mo.base

import android.graphics.Matrix

open class SpaceInfo(
    var x: Int = 0,
    var y: Int = 0,
    var w: Int = 0,
    var h: Int = 0,
) {
    // 增加矩阵
    var matrix: Matrix? = null

    val r: Int
        get() = x + w
    val b: Int
        get() = y + h

    fun setXY(x: Int, y: Int): SpaceInfo = apply {
        this.x = x
        this.y = y
    }

    fun setWH(w: Int, h: Int): SpaceInfo = apply {
        this.w = w
        this.h = h
    }

    fun set(x: Int, y: Int, w: Int, h: Int) = apply {
        this.x = x
        this.y = y
        this.w = w
        this.h = h
    }

    fun set(spaceInfo: SpaceInfo) = apply {
        this.x = spaceInfo.x
        this.y = spaceInfo.y
        this.w = spaceInfo.w
        this.h = spaceInfo.h
    }

    fun diffXY(dx: Int, dy: Int) {
        x += dx
        y += dy
    }

    fun contains(spaceInfo: SpaceInfo): Boolean {
        return true
    }

    fun intersects(spaceInfo: SpaceInfo): Boolean {
        return x < spaceInfo.r && spaceInfo.x < r && y < spaceInfo.b && spaceInfo.y < b
    }

    fun intersects(x: Float, y: Float): Boolean {
        return x >= this.x && x <= this.r && y >= this.y && y <= this.b
    }

    override fun equals(other: Any?): Boolean {
        return this === other ||
                (other is SpaceInfo
                        && (x == other.x && y == other.y && w == other.w && h == other.h))
    }
}