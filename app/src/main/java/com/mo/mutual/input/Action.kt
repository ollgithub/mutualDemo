package com.mo.mutual.input

import android.view.MotionEvent
import com.mo.isNew
import kotlin.math.abs

abstract class Action() {

    private var composeActions: ArrayList<Action>? = null

    // 返回是否响应
    abstract fun doAction(event: MotionEvent): Boolean

    abstract fun check(event: MotionEvent, inputProcessor: InputProcessor): Boolean

    fun next(action: Action) = apply {
        composeActions ?: let {
            composeActions = ArrayList()
        }
        composeActions?.add(action)
    }
}

class Touch(
    val withFingers: Boolean = false,
    val runn: ((Touch) -> Boolean)? = null,
) : Action() {

    var inputProcessor: InputProcessor? = null

    override fun check(
        event: MotionEvent,
        inputProcessor: InputProcessor
    ): Boolean {
        this.inputProcessor = inputProcessor
        return if (event.action == MotionEvent.ACTION_MOVE) {
            false
        } else {
            doAction(event)
        }
    }

    override fun doAction(event: MotionEvent): Boolean {
        return runn?.invoke(this) ?: false
    }
}

class Click(
    val runn: ((Click) -> Boolean)? = null,
) : Action() {
    var checking = false

    var event: MotionEvent? = null

    override fun check(event: MotionEvent, inputProcessor: InputProcessor): Boolean {
        if (checking.not() && event.isNew().not()) {
            return false
        }
        if (abs(inputProcessor.startPoint.x - event.x) >= 5
            || abs(inputProcessor.startPoint.y - event.y) >= 5
        ) {
            checking = false
            return false
        }
        checking = true
//        d("oooxxo check  $this")
        return if (checking && event.action == MotionEvent.ACTION_UP) {
            doAction(event)
        } else {
            false
        }
    }


    override fun doAction(event: MotionEvent): Boolean {
        this.event = event
        return runn?.invoke(this) ?: false
    }

}

class Long(
    val time: Int = 1000,
    val runn: ((Long) -> Boolean)? = null,
) : Action() {


    override fun check(event: MotionEvent, inputProcessor: InputProcessor): Boolean {
        return false
    }

    var checking = false

    override fun doAction(event: MotionEvent): Boolean {
        return runn?.invoke(this) ?: false
    }
}

class MoveAction(
    val fingerCount: Int = 1,
    val runn: ((MoveAction) -> Boolean)? = null,
) : Action() {
    var event: MotionEvent? = null
    var inputProcessor: InputProcessor? = null

    override fun check(event: MotionEvent, inputProcessor: InputProcessor): Boolean {
        this.inputProcessor = inputProcessor
        return if (event.action == MotionEvent.ACTION_MOVE) {
            doAction(event)
        } else {
            false
        }
    }

    override fun doAction(event: MotionEvent): Boolean {
        this.event = event
        return runn?.invoke(this) ?: false
    }
}

class Fling(
    angle: Float,
    val runn: ((Fling) -> Boolean)? = null,
) : Action() {

    override fun check(event: MotionEvent, inputProcessor: InputProcessor): Boolean {
        return false

    }

    override fun doAction(event: MotionEvent): Boolean {
        return runn?.invoke(this) ?: false
    }
}

// 定向移动，设置容忍度
class Direction(
    var angle: Float,
    var 双向: Boolean = false,
    var 容忍度: Float = 0.1f,
    var fingerCount: Int = 1,
    val runn: ((Direction) -> Boolean)? = null,
) : Action() {

    override fun check(event: MotionEvent, inputProcessor: InputProcessor): Boolean {

        return false
    }

    val a = 容忍度

    override fun doAction(event: MotionEvent): Boolean {
        return runn?.invoke(this) ?: false
    }

}

class Path(val runn: ((Action) -> Boolean)? = null) : Action() {


    override fun check(event: MotionEvent, inputProcessor: InputProcessor): Boolean {
        return false
    }

    override fun doAction(event: MotionEvent): Boolean {
        return runn?.invoke(this) ?: false
    }
}

//    class Key(val runn: ((MotionEvent) -> Boolean)? = null) : Action(runn) {}