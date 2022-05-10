package com.mo.mutual.orbit

import com.mo.mutual.MutualCenter
import com.mo.runMainPost

interface MutualOrbit {
    fun start()
}

/**
 * 临时设计,定时更新的动画驱动器
 */
class TimerOrbit(val time: Int) : MutualOrbit {

    val layout = MutualCenter.declaringLayout

    override fun start() {
        runMainPost(time.toLong()) {
            run()
        }
    }

    fun run() {
        layout?.redeclare()
        start()
    }

}