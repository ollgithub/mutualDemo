package com.mo.mutual.element

import android.graphics.SurfaceTexture
import android.media.MediaPlayer
import android.view.TextureView
import android.view.View
import com.mo.android.assist.CONTEXT
import com.mo.base.XY
import com.mo.e
import com.mo.mutual.Frame
import com.mo.mutual.layout.LayoutScope


fun LayoutScope.Texture(
): TextureMutual = declare {
    update()
}

class TextureMutual : ShapeMutual() {

    class TextureViewI : TextureView(CONTEXT) {
        init {
            val targetClass = TextureView::class.java
            val methods = targetClass.declaredMethods
            val fields = targetClass.declaredFields
            for (i in fields.indices) {
                fields[i].isAccessible = true
                e("f 反射得到的第 $i 个属性, ${fields[i]?.name}")
            }

            val fields2 = View::class.java.declaredFields
            for (i in fields2.indices) {
                fields2[i].isAccessible = true
                e("f 反射得到的第 $i 个属性, ${fields2[i]?.name}")
            }

            for (i in methods.indices) {
                val m = methods[i] ?: continue
                m.isAccessible = true
                e("f 反射得到的第 $i 个方法 ${m.name}")
                when (m.name) {
                    else -> {
                    }
                }
            }

        }


    }


    val g = TextureViewI()

    val rootPath = "/storage/emulated/0/"

    init {
    }

    val player = MediaPlayer().apply {
        setDataSource("${rootPath}2.mp4")
//        prepareAsync()
        isLooping = true
        setOnPreparedListener {
            it.start()
        }
    }

    fun update(
    ) {
    }

//    val texture = MutualView.textureView?.apply {
//        surfaceTextureListener = object :TextureView.SurfaceTextureListener{
//            override fun onSurfaceTextureAvailable(
//                surface: SurfaceTexture,
//                width: Int,
//                height: Int
//            ) {
//                d("ggggggggggg  $surface")
//                player.setSurface(Surface(surfaceTexture))
//            }
//
//            override fun onSurfaceTextureSizeChanged(
//                surface: SurfaceTexture,
//                width: Int,
//                height: Int
//            ) {
//
//            }
//
//            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture): Boolean {
//            return true
//            }
//
//            override fun onSurfaceTextureUpdated(surface: SurfaceTexture) {
////                MutualCenter.reDraw()
//            }
//
//        }
//    }

    var onPrepared: ((SurfaceTexture) -> Unit)? = null

    fun surfaceTexture(onPrepared: (SurfaceTexture) -> Unit) = apply {
        this.onPrepared = onPrepared
    }


    override fun onDraw(frame: Frame) {
//        texture.run {
//            layout(0,0,frame.currSpaceInfo.w,frame.currSpaceInfo.h)
//
//            draw(frame.frameImpl)
//
////           ( frame.frameImpl as RecordingCanvas).
//
//        }

    }

    override fun measureLayout(size: XY, fitContent: Boolean) {

    }
}

