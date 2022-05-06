package com.mo.core

import android.view.Choreographer
import com.mo.android.assist.Trace
import com.mo.consuming
import com.mo.currentTime
import com.mo.d
import com.mo.frame.core.Orbit

object OrbitCore {

    private val list = ArrayList<Orbit<*>>()

    private val choreographer: Choreographer = Choreographer.getInstance()

    fun callNext() {
        choreographer.postFrameCallback(this::update)
    }

    private fun update(frameTimeNanos: Long) {
        val currentTime: Long = currentTime()
        Trace.begin("OrbitCore_update")
        val iterator = list.iterator()
        while (iterator.hasNext()) {
            val orbit = iterator.next()
            val doing = orbit.doChange(currentTime)
            if (!doing) iterator.remove()
        }
        if (list.isNotEmpty()) {
            callNext()
        }
        Trace.end()
    }

    fun start(orbit: Orbit<*>) {
        if (list.contains(orbit).not()) {
            list.add(orbit)
            callNext()
        }
    }

    fun stop(orbit: Orbit<*>) {
        list.remove(orbit)
    }

}