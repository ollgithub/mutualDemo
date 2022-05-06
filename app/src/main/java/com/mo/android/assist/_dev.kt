package com.mo.android.assist

import android.app.Activity
import android.content.Context
import android.graphics.Bitmap
import android.widget.Toast
import com.mo.android.MutualActivity
import com.mo.android.MutualApp
import com.mo.d
import com.mo.runMainPost

val ACTIVITY: Activity
    get() = MutualActivity.ACTIVITY!!

val CONTEXT: Context
    get() = MutualApp.CONTEXT

val toast: Toast by lazy {
    Toast.makeText(CONTEXT, "", Toast.LENGTH_SHORT)
}

val Bitmap.s: String
    get() {
        return "Bitmap[w=${this.width},h=${this.height},config=${this.config},hash=${this.hashCode()}]"
    }

fun toast(string: String, longTime: Boolean = false) {
    val time = if (longTime) Toast.LENGTH_LONG else Toast.LENGTH_SHORT
    runMainPost {
        toast.apply {
            cancel()
            setText(string)
            duration = time
            show()
            d("oooo show")
        }
    }

}