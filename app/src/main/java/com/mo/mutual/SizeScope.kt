package com.mo.mutual

import com.mo.android.assist.traceBegin
import com.mo.android.assist.traceEnd
import com.mo.base.XY
import com.mo.f
import com.mo.i
import com.mo.mutual.layout.BaseScope
import java.lang.Float.min
import kotlin.math.ceil
import kotlin.math.max

class SizeScope : BaseScope() {

    val width: Int
        get() = size.x

    val height: Int
        get() = size.y

    private var size = XY()

    private var measureCall: ((XY, Boolean) -> Unit)? = null

    var relyContent = false
        private set

    var relyLayout = false
        private set

    private var measureSize: XY = XY(-1)
        get() {
            if (!relyContent) {
                field.set(size)
                traceBegin("measureSize")
                measureCall?.invoke(field, true)
                traceEnd()
                relyContent = true
            }
            return field
        }

    private fun clear() {
        size.set(0, 0)
        measureCall = null

        relyContent = true
        measureSize.set(0, 0)
        relyContent = false
        relyLayout = false
    }

    fun singleRun(
        containerSize: XY,
        measureCall: (XY, Boolean) -> Unit,
        sizeCall: (SizeScope.() -> Unit)? = null,
        isLayout: Boolean = false
    ) {
        clear()
        size.set(containerSize)
        this.measureCall = measureCall

        sizeCall?.let {
            run(it)
            containerSize.set(size)
        } ?: let {
            //没有配置尺寸规则时用content尺寸
            containerSize.set(measureSize)
        }
        // 如果没有在size运算中进行测量过,layout强制触发一次进行布局
        // TODO: 2022/1/7  仅现在没有适配layout的情况强制调用测试

        if (isLayout && !relyContent) {
            traceBegin("singleRun_measureSize")
            this.measureCall?.invoke(size, false)
            traceEnd()
        }

        this.measureCall = null
    }

    private fun applyScale(scale: Float) {
        size.set((measureSize.x * scale).i, (measureSize.y * scale).i)
    }

    fun fitShortest() {
        val scale = min(size.x / measureSize.x.f, size.y / measureSize.y.f)
        applyScale(scale)
    }

    fun fitLongest() {
        val scale = max(size.x / measureSize.x.f, size.y / measureSize.y.f)
        size.set(
            ceil(measureSize.x * scale).i,
            ceil(measureSize.y * scale).i
        )
    }

    fun fitInside() {
        val scale = max(1f, min(size.x / measureSize.x.f, size.y / measureSize.y.f))
        applyScale(scale)
    }

    /**
     * fitContent,fitLongest,fitShortest
     * 会触发一次测量,此时会对layout布局
     */
    fun fitContent() {
        size.set(measureSize)

    }

    fun scale(scale: Float) {
        size.set((size.x * scale).i, (size.y * scale).i)
    }

    /**
     * 如果layout是fitContent
     * 子mutual设置fitLayout
     * 得到的w,h将会是layout测量其他mutual总体尺寸后的结果
     * 否则得到的wh是layout的最大宽高
     */
    fun afterLayout() {
        relyLayout = true
    }

    fun fill() {
    }

    fun includeEffect() {
// TODO: 2022/1/16  
    }

    /**
     * 如果mutual的position规则限制了mutual的宽或高
     * 或者layout本身带有限制mutual宽高的性质
     * 设置将失效
     * 如果layout完全限制mutual宽高,sizeScope不会跑的
     */
    fun set(
        width: Int = size.x,
        height: Int = size.y
    ) {
        size.set(width, height)
    }

    fun set(
        wh: Int = Math.min(size.x, size.y)
    ) {
        size.set(wh, wh)
    }


}