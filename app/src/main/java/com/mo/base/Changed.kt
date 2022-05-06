package com.mo.base

import kotlin.reflect.KProperty

class Changed<T>(
    var value: T,
    private val setCall: (T) -> Unit,
) {

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return value
    }

    operator fun setValue(thisRef: Any?, property: KProperty<*>, newValue: T) {
        if (newValue == newValue) return
        value = newValue
        setCall(value)
    }

}