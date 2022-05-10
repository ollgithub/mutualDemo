package com.mo.mutual

import android.graphics.PointF
import com.mo.android.StackTrace
import com.mo.base.Color
import com.mo.core.Orbit
import com.mo.i
import com.mo.process
import kotlin.reflect.KProperty

fun <T> sync(
    value: T,
    withAnim: Boolean = true
): MutualSync<T> {
    return if (MutualCenter.isDeclaring) {
        val declarePosition = StackTrace.getCallKey(2)
        MutualCenter.cacheValue(declarePosition, value, withAnim)
    } else {
        MutualSync(value, withAnim)
    }
}

// TODO: 2022/1/29 lazy  init

class MutualSync<T>(
    var value: T,
    var withAnim: Boolean
) {

    val orbit: Orbit<T>? by lazy {
        val doChange: ((Float, T?) -> Unit)? = when {
            (orbitValue is Int) -> {
                { process: Float, end: T? ->
                    orbitValue = process((orbitValue as Int), (end as Int), process) as T
                    notifyUpdate()
                }

            }
            (orbitValue is Float) -> {
                { process: Float, end: T? ->
                    orbitValue = process((orbitValue as Float), (end as Float), process) as T
                    notifyUpdate()
                }

            }
            (orbitValue is Color) -> {
                { process: Float, end: T? ->
                    val a = (end as Color).argb()
                    val b = (orbitValue as Color).argb()
                    b.alpha = process(b.alpha, a.alpha, process)
                    b.red = process(b.red, a.red, process)
                    b.green = process(b.green, a.green, process)
                    b.blue = process(b.blue, a.blue, process)
                    orbitValue = b.color() as T
                    notifyUpdate()
                }
            }
            (orbitValue is PointF) -> {
                { process: Float, end: T? ->
                    val value = orbitValue as PointF
                    val end = end as PointF
                    orbitValue = PointF(
                        process(value.x, end.x, process),
                        process(value.y, end.y, process)
                    ) as T
                    notifyUpdate()
                }
            }
            else -> { process: Float, end: T? ->
                i("gogo doChange $process $end")
                orbitValue = end as T
                notifyUpdate()
            }
        }

        doChange?.let {
            Orbit(it)
        }

    }

    var orbitValue: T? = value

    private var changeListener = ArrayList<() -> Unit>()

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {

        val listener = changeListener
        MutualCenter.bindValue(listener)

        return if ((MutualCenter.isDeclaring || MutualCenter.isLayouting) && withAnim && orbit != null) {
            i("gogo getvalue $value $orbitValue")
            orbitValue as T
        } else value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>?, value: T) {
        if (value == this.value) return
        this.value = value

        if (withAnim) {
            orbit?.change(value, 450)
        }
        notifyUpdate()
    }

    private fun notifyUpdate() {
        val notifyLis = changeListener
        changeListener = ArrayList()

        notifyLis.forEach { it() }
        notifyLis.clear()

    }

}