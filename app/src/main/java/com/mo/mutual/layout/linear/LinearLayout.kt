package com.mo.mutual.layout.linear

import android.view.MotionEvent
import com.mo.base.Axis
import com.mo.base.XY
import com.mo.index
import com.mo.mutual.Frame
import com.mo.mutual.Mutual
import com.mo.mutual.layout.BaseMutualPackage
import com.mo.mutual.layout.Layout
import com.mo.mutual.layout.LayoutScope
import kotlin.math.max

fun LayoutScope.Row(
    gravity: Int = -1,
    averageBySize: Boolean = false,
    declareFunction: LinearScope.() -> Unit
): LinearLayout = declare {
    this.declareFunction = declareFunction
    update(
        Axis.X,
        gravity,
        averageBySize
    )
}

fun LayoutScope.Column(
    gravity: Int = -1,
    averageBySize: Boolean = false,
    declareFunction: LinearScope.() -> Unit
): LinearLayout = declare {
    this.declareFunction = declareFunction
    update(
        Axis.Y,
        gravity,
        averageBySize
    )
}

fun LayoutScope.Linear(
    direction: Int = Axis.X,
    gravity: Int = -1,
    averageBySize: Boolean = false,
    declareFunction: LinearScope.() -> Unit
): LinearLayout = declare {
    this.declareFunction = declareFunction
    update(
        direction,
        gravity,
        averageBySize
    )
}

class LinearLayout : Layout() {

    var declareFunction: (LinearScope.() -> Unit)? = null
        set(value) {
            field = value
            declare(containerLayout != null)
        }

    override val layoutScope = LinearScope(this)

    var direction: Int = Axis.X
    var gravity: Int = -1
    var averageBySize: Boolean = false

    /**
     * 0代表没有限制
     */
    fun update(
        direction: Int = Axis.X,
        gravity: Int = -1,
        averageBySize: Boolean = false
    ) {
        this.direction = direction
        this.gravity = gravity
        this.averageBySize = averageBySize
    }

    override fun onDeclare() {
        layoutScope.singleRun(this, declareFunction)
    }

    override fun moveMutual(mutual: Mutual, dx: Float, dy: Float) {
        TODO("Not yet implemented")
    }

    override fun onDraw(frame: Frame) {
        super.onDraw(frame)
    }

    override fun layout(
        size: XY,
        fitContent: Boolean
    ) {
        var pos = 0
        var maxH = 0
        var maxW = 0
        contents.forEachIndexed() { _, (mutual, _, spaceInfo) ->
            measureSingleMutual(
                size,
                mutual,
                spaceInfo
            )
            if (fitContent) {
                if (direction == Axis.X) {
                    maxH = max(maxH, spaceInfo.h)
                    maxW += spaceInfo.w
                } else {
                    maxH += spaceInfo.h
                    maxW = max(maxW, spaceInfo.w)
                }
            }
            if (direction == Axis.X) {
                val y = when (gravity) {
                    1 -> size.y - spaceInfo.h
                    0 -> (size.y - spaceInfo.h) / 2
                    else -> 0
                }
                spaceInfo.setXY(pos, y)
                pos += spaceInfo.w
            } else {
                val x = when (gravity) {
                    1 -> size.x - spaceInfo.w
                    0 -> (size.x - spaceInfo.w) / 2
                    else -> 0
                }
                spaceInfo.setXY(x, pos)
                pos += spaceInfo.h
            }
        }
        if (fitContent) {
            size.set(maxW, maxH)
            correctXYWithFinalSize(size)
        }

    }

    private fun correctXYWithFinalSize(size: XY) {
        if (gravity != 0 && gravity != 1) return
        contents.forEachIndexed() { _, (_, _, spaceInfo) ->
            if (gravity == 0) {
                if (direction == Axis.X) {
                    spaceInfo.y = (size.y - spaceInfo.h) / 2
                } else {
                    spaceInfo.x = (size.x - spaceInfo.w) / 2
                }
            } else {
                if (direction == Axis.X) {
                    spaceInfo.y = size.y - spaceInfo.h
                } else {
                    spaceInfo.x = size.x - spaceInfo.w
                }
            }
        }
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////  input

    override fun input(event: MotionEvent): Boolean {
        // TODO: 2022/1/26 使用，单次事件记录
        for (i in contents.size downTo 1) {
            val p = contents[i.index]
            if (p.spaceInfo.intersects(event.x, event.y)) {
                p.mutual.input(event)
            }
        }
        super.input(event)
        return true
    }
}