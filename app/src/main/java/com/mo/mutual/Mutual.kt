package com.mo.mutual

import android.view.MotionEvent
import com.mo.base.Color
import com.mo.base.XY
import com.mo.flag
import com.mo.i
import com.mo.mutual.ability.IInputProcessor
import com.mo.mutual.ability.IPainter
import com.mo.mutual.ability.ISizeRule
import com.mo.mutual.behavior.BehaviorScope
import com.mo.mutual.effect.Effect
import com.mo.mutual.input.Action
import com.mo.mutual.input.InputProcessor
import com.mo.mutual.layout.Layout
import com.mo.mutual.layout.transform.Transform
import com.mo.mutual.orbit.TimerOrbit
import com.mo.mutual.shader.ColorShader
import com.mo.mutual.shader.Shader
import com.mo.packHashCode

abstract class Mutual : ISizeRule, IPainter, IInputProcessor {

    companion object {
        val RELAYOUT_SHL = 1
        val RESIZE_SHL = 2
        val REDRAW_SHL = 3
        val REDECLARE_SHL = 4
    }

    var flag = 0
        protected set

    /**
     * mutual被标记relayout
     * 1 内容更新需要重新计算size
     * 2 sizeCall或sizeCall内部依赖更新
     * 3 layoutCall或layoutCall内部依赖更新
     */
    fun relayout() {

//        if (flag.flagHas(RELAYOUT_SHL)) return
        flag = flag.flag(RELAYOUT_SHL)

        containerLayout?.relayoutContent()

        MutualCenter.relayout()
//        MutualCenter.relayout()

    }

    fun redraw() {
        flag = flag.flag(REDRAW_SHL)
    }

    fun draw(frame: Frame) {
        shader?.let {
            if (it is ColorShader) {
                frame.mainPaint.color = it.color.value
            } else {
                frame.setShader(shader)
            }
        }
        frame.setEffect(effect)
        frame.mainPaint.alpha = (alpha * 255).i
        onDraw(frame)
        frame.setShader(null)
        frame.mainPaint.color = Color.BLACK.value
    }

    abstract fun onDraw(frame: Frame)

    abstract fun measureLayout(size: XY, fitContent: Boolean)

    open fun reportContentChange(vararg params: Any?) {
        MutualCenter.declaringLayout?.reportContentChange(this, packHashCode(params))
    }

    // todo orbit //////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////    behavior

    var containerLayout: Layout? = null

    ////////////////////////////////////////////////////////////////////////////////////////////////    behavior


    var floor = 0

    ////////////////////////////////////////////////////////////////////////////////////////////////    behavior
    var id: Int = -1

    fun markId(id: Int) = apply {
        this.id = id
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////    behavior

    var behaviorScope: BehaviorScope? = null

    fun behavior(behaviorCall: BehaviorScope.() -> Unit) = apply {
        behaviorScope ?: let {
            behaviorScope = BehaviorScope(this)
        }
        behaviorScope?.run(behaviorCall)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////   size

    override var sizeCall: (SizeScope.() -> Unit)? = null

    override var relyLayout: Boolean = false

    // TODO: 2021/12/22 容器有两种，1 是自由容器，2 限制尺寸和位置容器
    // TODO: 2021/12/22 在限制了尺寸的容器内尺寸参数是限制值
    // 没有size默认是content,有size默认是filling
    override fun size(
        relyLayout: Boolean,
        sizeCall: SizeScope.() -> Unit
    ) = apply {
        this.sizeCall = sizeCall

    }

    ////////////////////////////////////////////////////////////////////////////////////////////////   painter

    override var alpha: Float = 1f

    override var effect: Effect? = null

    override var shader: Shader? = null

    override fun blend(alpha: Float) = apply {
        this.alpha = alpha
    }

    override fun blend() = apply {

    }

    override fun effect(effect: Effect?) = apply {
        this.effect = effect
        //shadow
        // filterbitmap
        //Dither
        // light
        // inner light
    }

    override fun shader(shader: Shader) = apply {
        // bitmapshader
        // colorshader
        // gradiemtshader
        // compose
        this.shader = shader
    }

    override fun shader(color: Color) = apply {
        this.shader = ColorShader(color)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////   transform

    var transform: Transform? = null

    ////////////////////////////////////////////////////////////////////////////////////////////////   inputProcess

    private var inputProcessor: InputProcessor? = null

    override fun reaction(action: Action) = apply {
        inputProcessor ?: let {
            inputProcessor = InputProcessor()
            inputProcessor?.reaction(action)
        }
    }


    // TODO: 2022/2/26 不只是事件输入,可视输入,等等其他也有
    override fun input(event: MotionEvent): Boolean {
        return inputProcessor?.input(event) ?: false
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////

    fun inputFromLayout() {
        onShowingDoo?.invoke(true)
    }

    private var onShowingDoo: ((Boolean) -> Unit)? = null

    fun onShowing(doo: (Boolean) -> Unit) {
        onShowingDoo = doo
    }

    private var onWeakingDoo: ((Boolean) -> Unit)? = null

    fun onWeaking(doo: (Boolean) -> Unit) {
        onWeakingDoo = doo
    }

    var onLivingDoo: ((Boolean) -> Unit)? = null

    fun onLiving(doo: (Boolean) -> Unit) {
        onLivingDoo = doo
    }
    ////////////////////////////////////////////////////////////////////////////////////////////////

    private var orbit: Any? = null

    fun orbit(orbitItem: Any) = apply {
        if (orbit != null) return@apply
        orbit = orbitItem
        if (orbitItem is TimerOrbit) {
            orbitItem.start()
        }
    }
}



