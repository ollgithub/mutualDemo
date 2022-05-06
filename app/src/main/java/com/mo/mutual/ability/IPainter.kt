package com.mo.mutual.ability

import com.mo.base.Color
import com.mo.mutual.Mutual
import com.mo.mutual.effect.Effect
import com.mo.mutual.shader.Shader

interface IPainter {

    var alpha: Float

    var effect: Effect?

    var shader: Shader?

    fun blend(): Mutual

    fun blend(alpha: Float = 1f): Mutual

    fun effect(effect: Effect?): Mutual

    fun shader(shader: Shader): Mutual

    fun shader(color: Color): Mutual

}