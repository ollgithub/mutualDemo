package com.mo.android.assist

import com.mo.e
import java.lang.reflect.Method

fun Any.traceBegin(tag: String = this.javaClass.simpleName) {
    Trace.begin(tag)
}

fun Any.traceEnd() {
    Trace.end()
}

inline fun <T> Any.tracing(tag: String = this.javaClass.simpleName, call: () -> T): T {
    traceBegin(tag)
    val ans = call()
    traceEnd()
    return ans
}

object Trace {

    var debug = false

    private const val TRACE_TAG_ALWAYS = 1L shl 0

    private var beginFun: Method? = null
    private var endFun: Method? = null

    init {
        val targetClass = android.os.Trace::class.java
        val methods = targetClass.declaredMethods
        val fields = targetClass.declaredFields
        for (i in fields.indices) {
            fields[i].isAccessible = true
            e("反射得到的第 $i 个属性, ${fields[i]?.name}")
        }
        for (i in methods.indices) {
            val m = methods[i] ?: continue
            m.isAccessible = true
            e("反射得到的第 $i 个方法 ${m.name}")
            when (m.name) {
                "traceBegin" -> beginFun = m
                "traceEnd" -> endFun = m
                else -> {
                }
            }
        }

    }

    fun begin(tag: String) {
        if (!debug) return
        beginFun?.invoke(null, TRACE_TAG_ALWAYS, tag)
    }

    fun end() {
        if (!debug) return
        endFun?.invoke(null, TRACE_TAG_ALWAYS)
    }

}