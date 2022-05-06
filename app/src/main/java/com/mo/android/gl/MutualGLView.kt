package com.mo.android.gl

import android.content.Context
import android.graphics.Canvas
import android.opengl.GLSurfaceView
import android.os.Build
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import com.mo.base.SpaceInfo
import com.mo.base.XY
import com.mo.consuming
import com.mo.flagClear
import com.mo.mutual.Frame
import com.mo.mutual.Mutual
import com.mo.mutual.MutualCenter
import com.mo.mutual.layout.IMutualBase
import com.mo.mutual.layout.space.SpaceLayout
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class MutualGLView(
    private val space: SpaceLayout,
    context: Context
) : GLSurfaceView(context), IMutualBase, GLSurfaceView.Renderer {

    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        MutualCenter.mutualBase = this
    }

    private val spaceInfo = SpaceInfo()
    private val tempSize = XY()

    var drawing = false

    val mutualFrame = Frame()

    init {
        setEGLContextClientVersion(2)
        setEGLConfigChooser(8, 8, 8, 8, 16, 0)

        setRenderer(RendererManager(context, TextureRender(context)))
        renderMode = if (true) {
            GLSurfaceView.RENDERMODE_WHEN_DIRTY
        } else {
            GLSurfaceView.RENDERMODE_CONTINUOUSLY
        }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        consuming("timee  layout") {
            spaceInfo.set(left, top, right - left, bottom - top)
            space.measureLayout(tempSize.set(spaceInfo.w, spaceInfo.h), false)
        }
        MutualCenter.flag.flagClear(Mutual.RELAYOUT_SHL)
        invalidate()
    }

    @RequiresApi(Build.VERSION_CODES.Q)
    override fun onDraw(canvas: Canvas?) {
        canvas ?: return
        drawing = true
        mutualFrame.androidCanvas = canvas
        consuming("timee  ondraw") {
            space.draw(mutualFrame)
        }
        mutualFrame.androidCanvas = null

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return true
        space.input(event)
        if (event.action == MotionEvent.ACTION_UP) {
//            requestLayout()//
        }
        invalidate()
        return true
    }

    override fun relayout() {
        requestLayout()
    }

    override fun reDraw() {
        invalidate()
    }

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {


    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {

    }

    override fun onDrawFrame(gl: GL10?) {


    }

}