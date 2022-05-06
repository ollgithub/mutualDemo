package com.mo.mutual.shader

abstract class Shader {

    // TODO: 2022/1/5 需要解耦android
    abstract fun getShader(
        width: Int,
        height: Int
    ): android.graphics.Shader


}