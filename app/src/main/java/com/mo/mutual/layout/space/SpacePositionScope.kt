package com.mo.mutual.layout.space

import com.mo.base.Side
import com.mo.mutual.Mutual
import com.mo.mutual.MutualCenter
import com.mo.mutual.layout.PositionScope


data class SideRule(
    var margin: Int = 0,
    var id: Int = 0,
    var side: Int = 0
)

data class SpaceRule(
    var left: SideRule? = null,
    var top: SideRule? = null,
    var right: SideRule? = null,
    var bottom: SideRule? = null,
    var leftRightPercent: Float? = null,
    var topBottomPercent: Float? = null
)

class SpacePositionScope : PositionScope() {

    var spaceRule = SpaceRule()

    fun singleRun(
        mutual: Mutual,
        runn: SpacePositionScope.() -> Unit
    ) {
        clear()
        MutualCenter.layouting(mutual) {
            runn()
        }
    }

    fun clear() {
        spaceRule = SpaceRule()
    }


    fun top(
        margin: Int = 0,
        targetId: Int = 0,
        targetSide: Int = Side.TOP
    ) {
        spaceRule.run {
            if (top?.side != targetSide || top?.id != targetId) {
                topBottomPercent = null
            }
            top = SideRule(margin, targetId, targetSide)
        }

    }

    fun bottom(
        margin: Int = 0,
        targetId: Int = 0,
        targetSide: Int = Side.BOTTOM
    ) {
        spaceRule.run {
            if (bottom?.side != targetSide || bottom?.id != targetId) {
                spaceRule.topBottomPercent = null
            }
            bottom = SideRule(margin, targetId, targetSide)
        }
    }

    fun right(
        margin: Int = 0,
        targetId: Int = 0,
        targetSide: Int = Side.RIGHT
    ) {
        spaceRule.run {
            if (right?.side != targetSide || right?.id != targetId) {
                spaceRule.leftRightPercent = null
            }
            right = SideRule(margin, targetId, targetSide)
        }
    }


    fun left(
        margin: Int = 0,
        targetId: Int = 0,
        targetSide: Int = Side.LEFT
    ) {
        spaceRule.run {
            if (left?.side != targetSide || left?.id != targetId) {
                spaceRule.leftRightPercent = null
            }
            left = SideRule(margin, targetId, targetSide)
        }
    }

    fun margin(margin: Int) {
        margin(margin, margin, margin, margin)
    }

    fun margin(
        left: Int = 0,
        top: Int = 0,
        right: Int = 0,
        bottom: Int = 0,
    ) {
        spaceRule.run {
            leftRightPercent = null
            topBottomPercent = null
            this.left = SideRule(left, 0, Side.LEFT)
            this.top = SideRule(top, 0, Side.TOP)
            this.right = SideRule(right, 0, Side.RIGHT)
            this.bottom = SideRule(bottom, 0, Side.BOTTOM)
        }
    }

    /**
     * 和top,left冲突
     */
    fun vertical(
        percent: Float = 0.5f,
        topTargetId: Int = 0,
        topTargetSide: Int = Side.TOP,
        bottomTargetId: Int = 0,
        bottomTargetSide: Int = Side.BOTTOM
    ) {
        spaceRule.run {
            if (top?.side != topTargetSide || top?.id != topTargetId) {
                top = SideRule(0, topTargetId, topTargetSide)
            }
            if (bottom?.side != bottomTargetSide || bottom?.id != bottomTargetId) {
                bottom = SideRule(0, bottomTargetId, bottomTargetSide)
            }
            topBottomPercent = percent
        }
    }

    fun horizontal(
        percent: Float = 0.5f,
        leftTargetId: Int = 0,
        leftTargetSide: Int = Side.LEFT,
        rightTargetId: Int = 0,
        rightTargetSide: Int = Side.RIGHT
    ) {
        spaceRule.run {
            if (left?.side != leftTargetSide || left?.id != leftTargetId) {
                left = SideRule(0, leftTargetId, leftTargetSide)
            }
            if (right?.side != rightTargetSide || right?.id != rightTargetId) {
                right = SideRule(0, rightTargetId, rightTargetSide)
            }
            leftRightPercent = percent
        }
    }

    fun center() {
        margin(0)
        spaceRule.run {
            topBottomPercent = 0.5f
            leftRightPercent = 0.5f
        }
    }

}