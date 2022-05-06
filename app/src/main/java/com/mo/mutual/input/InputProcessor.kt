package com.mo.mutual.input

import android.graphics.PointF
import android.view.MotionEvent

class InputProcessor {

    fun MotionEvent.isNew(): Boolean {
        return this.action == MotionEvent.ACTION_DOWN
    }

    private val actions = ArrayList<Action>()


    var startPoint = PointF(0f, 0f)
        private set

    var lastPoint = PointF(0f, 0f)
        private set

    var dxy = PointF(0f, 0f)
        private set

//    private val mainHandler = Handler() {
//        when (it.what) {
//            1 -> {
//
//            }
//            else -> {
//
//            }
//        }
//        return@Handler true
//    }

    fun reaction(action: Action) {
        actions.clear()
        actions.add(action)
    }

    fun input(event: MotionEvent): Boolean {
        if (actions.size == 0) return false
        if (event.isNew()) {
            startPoint.set(event.x, event.y)
            lastPoint.set(0f, 0f)
            dxy.set(0f, 0f)
        } else {
            dxy.set(event.x - lastPoint.x, event.y - lastPoint.y)
        }
        var ans = false
        actions.forEach {
            val oneActionAns = it.check(event, this)
            ans = ans or oneActionAns
        }
        lastPoint.set(event.x, event.y)
        return ans
    }

}
