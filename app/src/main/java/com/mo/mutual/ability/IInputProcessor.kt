package com.mo.mutual.ability

import android.view.MotionEvent
import com.mo.mutual.Mutual
import com.mo.mutual.input.Action

interface IInputProcessor {

    fun reaction(action: Action): Mutual

    fun input(event: MotionEvent): Boolean

}