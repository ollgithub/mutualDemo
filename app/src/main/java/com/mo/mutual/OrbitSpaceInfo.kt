package com.mo.mutual

import com.mo.base.SpaceInfo
import com.mo.core.Orbit
import com.mo.d
import com.mo.e
import com.mo.process

class OrbitSpaceInfo(
    x: Int = 0,
    y: Int = 0,
    w: Int = 0,
    h: Int = 0,
    var alpha: Float = 1f
) : SpaceInfo(x, y, w, h) {

    var currentV = 0f

    val orbit = Orbit<Float>(
        { process: Float, end: Float? ->
            e("ooooooo change xxxxxxxx $process")
            currentV = process
            MutualCenter.reDraw()
        }
    )

    var changing = SpaceInfo()


    val end = SpaceInfo()

    fun buildOrbit() {
        if (end.equals(this)) return
        e("ooooooo buildOrbit $this")
        end.set(this)
//        orbit.change(1f, 350)

    }

    fun getOrbitInfo(): SpaceInfo {

        return if (orbit.runing) {
            d("ooooooo $this $currentV")
            changing.set(
                process(changing.x, end.x, currentV),
                process(changing.y, end.y, currentV),
                end.w,
                end.h
            )
        } else this
    }


}