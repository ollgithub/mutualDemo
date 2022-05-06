package com.mo.mutual.layout.grid

import com.mo.base.Axis
import com.mo.base.XY
import com.mo.mutual.Frame
import com.mo.mutual.Mutual
import com.mo.mutual.layout.BaseMutualPackage
import com.mo.mutual.layout.Layout
import com.mo.mutual.layout.LayoutScope


//fun Grid(
//    yCount: Int = 0,
//    xCount: Int = 1,
//    extendAxis: Axis = Axis.Y,
//    declareFunction: GridScope.() -> Unit
//) = GridLayout().apply {
//    this.declareFunction = declareFunction
//    update(
//        yCount, xCount, extendAxis
//    )
//}

fun LayoutScope.Grid(
    yCount: Int = 0,
    xCount: Int = 1,
    extendAxis: Int = Axis.Y,
    declareFunction: GridScope.() -> Unit
): GridLayout = declare {
    this.declareFunction = declareFunction
    update(
        yCount, xCount, extendAxis
    )
}

class GridLayout : Layout() {

    var declareFunction: (GridScope.() -> Unit)? = null
        set(value) {
            field = value
            declare(containerLayout != null)
        }

    override val layoutScope = GridScope(this)

    var yCount = 0
    var xCount = 1
    var extendAxis = Axis.Y

    /**
     * 0代表没有限制
     */
    fun update(
        yCount: Int = 0,
        xCount: Int = 1,
        extendAxis: Int = Axis.Y,
    ) {
        this.yCount = yCount
        this.xCount = xCount
        this.extendAxis = extendAxis

    }

    override fun onDeclare() {
        layoutScope.singleRun(this, declareFunction)
    }

    val tempSize = XY()

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
        if (xCount == 0 || yCount == 0) {
            return
        }
        val itemWidth = size.x / yCount
        val itemHeight = size.y / xCount

        contents.forEachIndexed() { index, (mutual, _, spaceInfo) ->
            val xN = index % yCount
            val yN = index / yCount
            val x = xN * itemWidth
            val y = yN * itemHeight
            spaceInfo.set(x, y, itemWidth, itemHeight)
            measureSingleMutual(
                tempSize.set(itemWidth, itemHeight),
                mutual,
                spaceInfo
            )
        }
    }

}