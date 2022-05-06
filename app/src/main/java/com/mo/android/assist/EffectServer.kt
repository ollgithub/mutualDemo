package com.mo.android.assist

import android.graphics.Bitmap
import android.renderscript.Allocation
import android.renderscript.Element
import android.renderscript.RenderScript
import android.renderscript.ScriptIntrinsicBlur

object EffectServer {

    fun blur(inputBitmap: Bitmap, radius: Float = 5f): Bitmap {
        kotlin.runCatching {
            val outputBitmap: Bitmap = Bitmap.createBitmap(inputBitmap)

            //创建RenderScript，ScriptIntrinsicBlur固定写法
            val rs: RenderScript = RenderScript.create(CONTEXT)
            val blurScript: ScriptIntrinsicBlur = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs))


            //根据inputBitmap，outputBitmap分别分配内存
            val tmpIn: Allocation = Allocation.createFromBitmap(rs, inputBitmap)
            val tmpOut: Allocation = Allocation.createFromBitmap(rs, outputBitmap)


            //设置模糊半径取值0-25之间，不同半径得到的模糊效果不同
            blurScript.setRadius(5f)
            blurScript.setInput(tmpIn)
            blurScript.forEach(tmpOut)
            //得到最终的模糊bitmap
            tmpOut.copyTo(outputBitmap)
            return outputBitmap

        }.onFailure {
            it.printStackTrace()
        }
        return inputBitmap
    }

}