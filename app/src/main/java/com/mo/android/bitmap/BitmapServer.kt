package com.mo.android.bitmap

import android.graphics.BitmapFactory
import com.mo.base.XY
import java.io.File
import java.io.FileInputStream

/*
计划
1. 实现不同优先级的缓存池策略
2. 根据显示区域加载图片或做图片裁剪
3. 实现bitmap的缓存
4. 实现bitmap的特效应用


 */
object BitmapServer {

    fun getImageWH(file: File): XY? {
        runCatching {
            val options = BitmapFactory.Options().apply {
                inJustDecodeBounds = true
            }
            FileInputStream(file).use {
                BitmapFactory.decodeStream(it, null, options)
            }
            val oriHeight = options.outHeight
            val oriWidth = options.outWidth
            return XY(oriWidth, oriHeight)
        }.onFailure {
            it.printStackTrace()
        }
        return null
    }

}