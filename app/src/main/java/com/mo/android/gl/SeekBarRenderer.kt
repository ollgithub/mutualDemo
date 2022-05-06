package com.mo.android.gl

import android.opengl.GLSurfaceView
import android.widget.SeekBar
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

open class SeekBarRenderer : GLSurfaceView.Renderer {

    open var progress = floatArrayOf(0.7f, 0f, 0f, 0f, 0f)

    open fun onEvent(index: Int) {}

    override fun onDrawFrame(gl: GL10?) {
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {

    }

    open fun onDestroy() {}

}

abstract class SeekBarAdapter : SeekBar.OnSeekBarChangeListener {
    override fun onProgressChanged(
        seekBar: SeekBar?,
        progress: Int,
        fromUser: Boolean
    ) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {}

    override fun onStopTrackingTouch(seekBar: SeekBar?) {}
}