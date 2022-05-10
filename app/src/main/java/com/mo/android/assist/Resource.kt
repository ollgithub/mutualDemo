package com.mo.android.assist

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import com.mo.base.Color

fun Int.bitmap(): Bitmap = BitmapFactory.decodeResource(CONTEXT.resources, this)
fun Int.color(): Color = Color(CONTEXT.resources.getColor(this, null))
fun Int.font(): Typeface = CONTEXT.resources.getFont(this)

fun Int.rDp() = ACTIVITY.resources.getDimensionPixelSize(this)

val Int.dp: Int
    get() = (this * WindowServer.density + 0.5f).toInt()


val Int.dpf: Float
    get() = this * WindowServer.density + 0.5f