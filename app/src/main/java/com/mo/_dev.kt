package com.mo

import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.Rect
import android.graphics.RectF
import android.net.Uri
import android.os.Looper
import android.util.Log
import android.view.MotionEvent
import com.mo.Dev.debug
import com.mo.Dev.lastMarkTime
import com.mo.android.assist.Trace
import com.mo.base.XY
import com.mo.core.Core
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*

object Dev {
    var lastMarkTime = 0L
    val debug: Boolean? = true
    var debugInfo = StringBuilder("")

    var onText: ((String) -> Boolean)? = null

    fun add(info: String) {
        debugInfo.appendln(info)
        if (debugInfo.length > 2000) {
            debugInfo.delete(0, 100)
        }
        i(debugInfo.toString())
        onText?.invoke(debugInfo.toString())
    }
}

data class Stack(
    val className: String,
    val methodName: String,
)


inline val Int.index: Int get() = this - 1

inline val Int.f: Float get() = this.toFloat()

inline val Float.i: Int get() = this.toInt()


fun Cursor.getContent(goal: String): String {
    return this.getString(getColumnIndex(goal)) ?: let {
//        //  e("cursor getContent empty , the goal = $goal")
        ""
    }
}

inline fun Int.flag(shlNum: Int) = this or (1 shl shlNum)

inline fun Int.flagClear(shlNum: Int) = this and (1 shl shlNum).inv()

inline fun Int.flagHas(shlNum: Int) = (this and (1 shl shlNum)) shr shlNum == 1

inline fun Uri.getContentUri(id: String): Uri {
    return this.buildUpon().appendPath(id).build()
}

fun markTime() {
    debug ?: return
    val (className, methodName) = getStackName()
    val time = System.currentTimeMillis()
    Log.e(
        className,
        "<$methodName> time = ${Date(time)} $time ms = ${time % 100} (since last mark = ${time - lastMarkTime})"
    )
    lastMarkTime = time
}

inline infix fun Int.nfor(function: (Int) -> Unit) {
    for (i in 1..this) {
        function.invoke(i)
    }
}

inline fun <T> runCatchingPrint(function: () -> T): T? {
    return try {
        Trace.begin("runCatchingPrint")
        function.invoke()
    } catch (e: Exception) {
        e.printStackTrace()
        null
    } finally {
        Trace.end()
    }
}

fun Any?.isNotNull(): Boolean {
    return this != null
}

fun currentTime() = System.currentTimeMillis()

fun consuming(startTime: Long) = currentTime() - startTime

fun Any.consuming(tag: String? = null, function: () -> Unit) {
    if (debug == null) {
        return function.invoke()
    }
    Log.e(this.javaClass.name, "<consuming> $tag consuming start")
    val startTime = System.currentTimeMillis()
    Trace.begin("$tag")
    function.invoke()
    Trace.end()
    Log.e(this.javaClass.name, "<consuming> $tag consuming = ${getConsumingTime(startTime)}")
}

fun Any.d(string: String) {
    debug ?: return
    Log.d(this.javaClass.name, string)
}

fun Any.e(string: String) {
    Log.e(this.javaClass.name, string)
}

fun Any.i(string: String) {
    debug ?: return
    Log.i(this.javaClass.name, string)
}

fun Any.v(string: String) {
    debug ?: return
    Log.v(this.javaClass.name, string)
}

fun getStackName(): Stack {
    val stack = Thread.currentThread().stackTrace[2]
    return Stack(stack.className, stack.methodName)
}

fun getCallPosition(): StackTraceElement {
    return Thread.currentThread().stackTrace[4]
}

fun getConsumingTime(startTime: Long) = System.currentTimeMillis() - startTime

fun addDebug(info: String) {
    Dev.add(info)
}

fun <T> runAsyncNull(runn: () -> Unit): T? {
    runAsync(function = runn)
    return null
}


fun runAsync(delayTime: Long = 0, function: () -> Unit) {
    GlobalScope.launch(Dispatchers.Default) {
        if (delayTime != 0L) {
            delay(delayTime)
        }
        runCatchingPrint(function)
    }
}

fun runIO(delayTime: Long = 0, function: () -> Unit) {
    GlobalScope.launch(Dispatchers.IO) {
        if (delayTime != 0L) {
            delay(delayTime)
        }
        runCatchingPrint(function)
    }
}

fun runMainPost(delayTime: Long = 0, function: (() -> Unit)) {
    runMain(delayTime, true, function)
}

fun runMain(delayTime: Long = 0, post: Boolean = false, function: (() -> Unit)) {
    if (delayTime == 0L) {
        if (Looper.getMainLooper().isCurrentThread && !post) {
            runCatchingPrint(function)
        } else {
            Core.mainHandler.post {
                Trace.begin("runMain")
                function()
                Trace.end()
            }
        }
    } else {
        Core.mainHandler.postDelayed({
            Trace.begin("runMainDelay")
            function()
            Trace.end()
        }, delayTime)
    }
}

fun <T> loopSwitch(now: T, vararg states: T): T {
    var next = false
    for (i in states) {
        if (next) {
            return i
        }
        next = now == i
    }
    return states[0]
}

fun packHashCode(vararg other: Any?): Int {
    var h = 0
    other.forEach {
        h = h * 31 + it.hashCode()
    }
    return h
}

fun XY.noZeroSize(): XY {
    if (this.x <= 0) {
        this.x = 1
    }
    if (this.y <= 0) {
        this.y = 1
    }
    return this
}

fun XY.rotate90(): XY {
    val h = this.y
    this.y = this.x
    this.x = h
    return this
}

fun Int.is90Degree(): Boolean {
    return (this / 90) % 2 == 1
}

fun printStack(tag: String = "") {
    runCatchingPrint {
        throw Exception(tag)
    }
}

fun RectF.intersects(
    rectF: RectF,
): Boolean {
    return intersects(rectF.left, rectF.top, rectF.right, rectF.bottom)
}

fun RectF.intersects(
    rect: Rect,
): Boolean {
    return intersects(
        rect.left.toFloat(),
        rect.top.toFloat(),
        rect.right.toFloat(),
        rect.bottom.toFloat()
    )
}

fun RectF.setBitmapSize(
    bitmap: Bitmap,
) {
    set(0f, 0f, bitmap.width.toFloat(), bitmap.height.toFloat())
}

fun Rect.intersects(
    rect: Rect,
): Boolean {
    return intersects(rect.left, rect.top, rect.right, rect.bottom)
}

fun Rect.setBitmapSize(
    bitmap: Bitmap,
) {
    set(0, 0, bitmap.width, bitmap.height)
}


fun process(start: Int, end: Int, progress: Float): Int {
    return (start + (end - start) * progress).toInt()
}

fun process(start: Char, end: Char, progress: Float): Char {
    return (start + ((end.code - start.code) * progress).toInt())
}

fun process(start: Float, end: Float, progress: Float): Float {
    return start + (end - start) * progress
}

val Float.half get() = this / 2f

val Int.half get() = this / 2

val Int.halfF get() = this / 2f

inline infix fun Boolean.`true`(function: () -> Unit): Boolean {
    if (this) function.invoke()
    return this
}

inline infix fun Boolean.`false`(function: () -> Unit): Boolean {
    if (!this) function.invoke()
    return this
}

fun MotionEvent.isNew(): Boolean {
    return this.action == MotionEvent.ACTION_DOWN
}
