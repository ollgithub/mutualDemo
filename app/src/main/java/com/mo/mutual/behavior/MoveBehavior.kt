package com.mo.mutual.behavior

import android.graphics.PointF
import com.mo.mutual.Mutual
import com.mo.mutual.input.MoveAction


fun BehaviorScope.Move() {
    bind(MoveBehavior())
}

class MoveBehavior : LayoutBehavior() {

    var mutual: Mutual? = null

    override fun bind(mutual: Mutual) {
        this.mutual = mutual
        mutual.reaction(MoveAction {
            move(it.inputProcessor!!.dxy)
            true
        })

    }

    fun move(dxy: PointF) {
        mutual?.let {
            it.containerLayout?.moveMutual(it, dxy.x, dxy.y)
        }

    }

}