package com.mo.android

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Typeface
import com.mo.android.assist.ACTIVITY
import com.mo.android.assist.CONTEXT
import com.mo.android.assist.WindowServer

fun Int.rBitmap(): Bitmap = BitmapFactory.decodeResource(CONTEXT.resources, this)
fun Int.rFont(): Typeface = CONTEXT.resources.getFont(this)

fun Int.rDp() = ACTIVITY.resources.getDimensionPixelSize(this)

val Int.dp: Int
    get() = (this * WindowServer.density + 0.5f).toInt()