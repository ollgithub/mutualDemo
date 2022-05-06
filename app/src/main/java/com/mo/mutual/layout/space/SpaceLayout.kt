package com.mo.mutual.layout.space

import android.view.MotionEvent
import com.mo.*
import com.mo.android.assist.traceBegin
import com.mo.android.assist.traceEnd
import com.mo.base.XY
import com.mo.mutual.Mutual
import com.mo.mutual.layout.BaseMutualPackage
import com.mo.mutual.layout.Layout
import com.mo.mutual.layout.LayoutScope
import kotlin.math.max
import kotlin.math.min

fun Space(
    declareFunction: SpaceScope.() -> Unit
) = SpaceLayout().apply {
    this.declareFunction = declareFunction
}

fun LayoutScope.Space(
    declareFunction: SpaceScope.() -> Unit
): SpaceLayout = declare {
    this.declareFunction = declareFunction
}

// TODO: 2022/3/3 后续功能 重力
class SpaceLayout : Layout() {

    companion object {
        private const val RB = 10E8.toInt()
        private const val RB_LEFT = 5E8.toInt()
    }

    var declareFunction: (SpaceScope.() -> Unit)? = null
        set(value) {
            field = value
//            if(containerLayout!=null) {
            declare(true)//containerLayout != null)
//            }
        }

    override val contents: ArrayList<SpaceMutualPackage>
        get() = layoutScope.contents

    override val layoutScope: SpaceScope = SpaceScope(this)
    ////////////////////////////////////////////////////////////////////////////////////////////////  declare

    override fun packMutual(mutual: Mutual, declareKey: Int): BaseMutualPackage {
        return SpaceMutualPackage(mutual, declareKey)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////  declare

    override fun onDeclare() {
        layoutScope.singleRun(this, declareFunction)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////  move

    override fun moveMutual(mutual: Mutual, dx: Float, dy: Float) {
        val m = contents.find { it.mutual == mutual } ?: return
        m.spaceInfo.y += dy.toInt()
//        m.spaceInfo.x = max(0, m.spaceInfo.x+ dx.toInt())
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

    ////////////////////////////////////////////////////////////////////////////////////////////////  layout

    /**
     * 规则
     * 1 如果子mutual是fitcontent,且有约束,优先计算约束,用约束后尺寸去测量
     * 3 一旦尺寸发生变化,需要全部重新run,因为子mutual的尺寸和layout尺寸有依赖
     * 4 一旦有约束,对应宽高将被强制指定
     */
    override fun layout(
        size: XY,
        fitContent: Boolean
    ) {
        if (fitContent) {
            traceBegin("measureSelf")
            val changed = measureLayoutNoRely(size)
            traceEnd()

            traceBegin("measureSelf")
            correctLayoutAfterMeasure(size)
            traceEnd()
        } else {
            layoutAllWithFixSize(size)
        }
        contents.forEach {
            it.spaceInfo.buildOrbit()
        }
    }

    private fun HashMap<Int, SpaceMutualPackage>?.getPos(id: Int, side: Int): Int {
        val spaceInfo = this?.get(id)?.spaceInfo ?: this@SpaceLayout.spaceInfo
        return when (side) {
            0 -> spaceInfo.x
            1 -> spaceInfo.y
            2 -> spaceInfo.r
            else -> spaceInfo.b
        }
    }

    private fun measureLayoutOne(
        spaceMutualPackage: SpaceMutualPackage,
        tempLimitSize: XY,
        size: XY,
        justPercent: Boolean = false
    ): XY {

        var left = 0
        var top = 0
        val map = layoutScope.idMap
        val (mutual, _, thisSpaceInfo, _, rule) = spaceMutualPackage
        if (rule == null) {
            measureSingleMutual(tempLimitSize, mutual, thisSpaceInfo)
        } else {
            var right = 0
            var bottom = 0

            var xLimit = false
            var yLimit = false

            rule.left?.let { ruleLeft ->
                left = map.getPos(ruleLeft.id, ruleLeft.side) + ruleLeft.margin
                xLimit = true
            }
            rule.right?.let { ruleRight ->
                right = map.getPos(ruleRight.id, ruleRight.side) - ruleRight.margin
                xLimit = true
            }

            rule.top?.let { ruleTop ->
                top = map.getPos(ruleTop.id, ruleTop.side) + ruleTop.margin
                yLimit = true
            }
            rule.bottom?.let { ruleBottom ->
                bottom = map.getPos(ruleBottom.id, ruleBottom.side) - ruleBottom.margin
                yLimit = true
            }

            if (xLimit) {
                tempLimitSize.x = when {
                    (left < RB_LEFT && right < RB_LEFT) -> right - left
                    (left < RB_LEFT && right >= RB_LEFT) -> size.x - left - (RB - right)
                    (left >= RB_LEFT && right < RB_LEFT) -> right - (size.x - (RB - left))
                    else -> right - left
                }
            }

            if (yLimit) {
                tempLimitSize.y = when {
                    (top < RB_LEFT && bottom < RB_LEFT) -> bottom - top
                    (top < RB_LEFT && bottom >= RB_LEFT) -> size.y - top - (RB - bottom)
                    (top >= RB_LEFT && bottom < RB_LEFT) -> bottom - (size.y - (RB - top))
                    else -> bottom - top
                }
            }

            if (!justPercent) {
                measureSingleMutual(tempLimitSize, mutual, thisSpaceInfo)
            }

            if (rule.left == null && rule.right != null) {
                left = right - thisSpaceInfo.w
            }
            if (rule.top == null && rule.bottom != null) {
                top = bottom - thisSpaceInfo.h
            }


            rule.leftRightPercent?.let { percent ->
                if ((left < RB_LEFT && right < RB_LEFT) || (left >= RB_LEFT && right >= RB_LEFT)) {
                    left += ((tempLimitSize.x - thisSpaceInfo.w) * percent).i
                }

//                left += ((tempLimitSize.x - thisSpaceInfo.w) * percent).i
            }
            rule.topBottomPercent?.let { percent ->
                if ((top < RB_LEFT && bottom < RB_LEFT) || (top >= RB_LEFT && bottom >= RB_LEFT)) {
                    top += ((tempLimitSize.y - thisSpaceInfo.h) * percent).i
                }
//                top += ((tempLimitSize.y - thisSpaceInfo.h) * percent).i
            }

        }
        return XY(left, top)
    }

    private fun measureLayoutNoRely(size: XY): Boolean {

        val tempMeasureSize = XY()
        val tempLimitSize = XY()

        spaceInfo.setWH(RB, RB)
        contents.forEach { spaceMutualPackage ->

            if (spaceMutualPackage.mutual.relyLayout) return@forEach

            tempLimitSize.set(size)

            val (left, top) = measureLayoutOne(
                spaceMutualPackage,
                tempLimitSize,
                size
            )
            spaceMutualPackage.spaceInfo.setXY(left, top)

            // 遍历取大,不能超过limit
            val thisTimeWidth = if (left < RB_LEFT) {
                left + spaceMutualPackage.spaceInfo.w
            } else {
                RB - left
            }
            val thisTimeHeight = if (top < RB_LEFT) {
                top + spaceMutualPackage.spaceInfo.h
            } else {
                RB - top
            }
//            val thisTimeWidth =  size.x - (tempLimitSize.x - spaceMutualPackage.spaceInfo.w)
//
//            val thisTimeHeight =  size.y - (tempLimitSize.y - spaceMutualPackage.spaceInfo.h)


            tempMeasureSize.set(
                max(
                    tempMeasureSize.x,
                    min(size.x, thisTimeWidth)
                ),
                max(
                    tempMeasureSize.y,
                    min(size.y, thisTimeHeight)
                )
            )
        }

        tempLimitSize.set(tempMeasureSize)

        // 如果尺寸不相等,说明layout尺寸变更了
        val ans = tempLimitSize != size
        size.set(tempLimitSize)
        spaceInfo.setWH(size.x, size.y)
        return ans
    }

    private fun correctLayoutAfterMeasure(
        size: XY
    ) {
        val tempLimitSize = XY()
        contents.forEach { spaceMutualPackage ->
            tempLimitSize.set(size)
            if (spaceMutualPackage.mutual.relyLayout) {

                val (left, top) = measureLayoutOne(
                    spaceMutualPackage,
                    tempLimitSize,
                    size
                )
                spaceMutualPackage.spaceInfo.setXY(left, top)
            } else {
                spaceMutualPackage.spaceInfo.let {
                    if (it.x > RB_LEFT) {
                        it.x = size.x - (RB - it.x)
                    }
                    if (it.y > RB_LEFT) {
                        it.y = size.y - (RB - it.y)
                    }
                }
                if (spaceMutualPackage.rule?.leftRightPercent != null
                    || spaceMutualPackage.rule?.topBottomPercent != null
                ) {
                    val (left, top) = measureLayoutOne(
                        spaceMutualPackage,
                        tempLimitSize,
                        size,
                        true
                    )
                    spaceMutualPackage.spaceInfo.setXY(left, top)
                }
            }
        }
    }

    private fun layoutAllWithFixSize(
        size: XY
    ) {
        val tempLimitSize = XY()
        contents.forEach { spaceMutualPackage ->
            tempLimitSize.set(size)
            val (left, top) = measureLayoutOne(
                spaceMutualPackage,
                tempLimitSize,
                size,
                false
            )
            spaceMutualPackage.spaceInfo.setXY(left, top)
        }
    }

}