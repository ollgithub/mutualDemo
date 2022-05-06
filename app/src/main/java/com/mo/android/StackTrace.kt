package com.mo.android

import com.mo.e
import java.lang.reflect.Field

object StackTrace {

    private val obj = Throwable()
    private var backtrace: Field

    init {
        val targetClass = obj::class.java
        val methods = targetClass.declaredMethods
        val fields = targetClass.declaredFields
        for (i in fields.indices) {
            fields[i].isAccessible = true
            e("反射得到的第 $i 个属性, ${fields[i]?.name}")
        }
        for (i in methods.indices) {
            methods[i]?.isAccessible = true
            e("反射得到的第 $i 个方法 ${methods[i]?.name}")
        }
        backtrace = fields[0]
    }

    fun getCallKey(index: Int): Int {
        obj.fillInStackTrace()
        ((backtrace.get(obj) as Array<*>)[0] as LongArray).apply {
            return get(size / 2 + index + 1).toInt()
        }
    }

}