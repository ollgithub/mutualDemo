package com.mo.android

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.Canvas
import android.graphics.RenderNode
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import com.mo.*
import com.mo.base.Color
import com.mo.base.XY
import com.mo.mutual.Frame
import com.mo.mutual.Mutual
import com.mo.mutual.MutualCenter
import com.mo.mutual.layout.IMutualBase
import com.mo.mutual.layout.Layout
import com.mo.mutual.layout.linear.LinearLayout
import com.mo.mutual.layout.space.Space
import com.mo.mutual.layout.space.SpaceLayout

@SuppressLint("ViewConstructor")
class MutualView(
    context: Context,
    val space: Layout = Space { }
) : View(context), IMutualBase {


    init {
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        MutualCenter.mutualBase = this
    }

    private val size = XY()

    var drawing = false

    val mutualFrame = Frame()

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {

        consuming("timee  layout") {
            space.measureLayout(size.set(right - left, bottom - top), false)
        }
        invalidate()


        printLayoutTree()
    }

    private fun printLayoutTree() {
        val builder = StringBuilder()
        printSingleLayout(builder, "", space)
        d("LayoutPrint \n$builder")
    }

    private fun printSingleLayout(builder: StringBuilder, empty: String, layout: Layout) {
        val myEmpty = "$empty    "
        for (i in layout.contents) {
            val id = if (i.mutual.id > 0)
                "(${context.resources.getResourceName(i.mutual.id)})"
            else
                ""
            builder.append("$myEmpty │─ ${i.mutual.javaClass.simpleName} $id: ${i.spaceInfo} \n")
            if (i.mutual is Layout) {
                printSingleLayout(builder, myEmpty, i.mutual)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.Q)
    val renderNode = RenderNode("go").apply {
        this.setPosition(0, 0, 1000, 1000)
        val a = this.beginRecording()
        a.drawColor(Color.RED.value)
        this.endRecording()
    }

    @SuppressLint("MissingSuperCall")
    @RequiresApi(Build.VERSION_CODES.Q)
    override fun draw(canvas: Canvas?) {
        canvas ?: return
        drawing = true
        mutualFrame.androidCanvas = canvas
        consuming("timee  ondraw") {
            space.draw(mutualFrame)
//            canvas.drawRenderNode(renderNode)
        }
        mutualFrame.androidCanvas = null

    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        event ?: return true
        space.input(event)
        if (event.action == MotionEvent.ACTION_UP) {
            requestLayout()//
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

}