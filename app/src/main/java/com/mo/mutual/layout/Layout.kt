package com.mo.mutual.layout

import com.mo.*
import com.mo.android.assist.traceBegin
import com.mo.android.assist.traceEnd
import com.mo.base.SpaceInfo
import com.mo.base.XY
import com.mo.mutual.*
import com.mo.mutual.ability.IDebug

/**
 * layout是布局者
 *
 * 提供其他mutual的承载和规划
 * 包含能力:
 *      声明环境
 *      位置说明环境
 *      尺寸说明环境(通用)
 *      布局绘制安排
 *      动画计算能力
 */
abstract class Layout : Mutual(), IDebug {

    // TODO: 2021/12/29
    //  1 移动元素
    //  2 移动镜头
    //  3 限制容器宽高
    //  4 镜头调整（缩放，旋转。。）
    //  5 实际容器尺寸
    //  6 限制子元素尺寸
    //  7 限制子元素绘制（全限制）
    //  8 新增子元素位置（不允许重叠）
    //  9 折叠

    abstract val layoutScope: LayoutScope

    protected val sizeScope = SizeScope()

    open val contents: List<BaseMutualPackage>
        get() = layoutScope.baseContents

    var spaceInfo: OrbitSpaceInfo = OrbitSpaceInfo()

    var fitContent = false

    private val tempSize = XY(0, 0)

    open fun findMutual(position: XY): Mutual? {
        return null
    }

    abstract fun moveMutual(mutual: Mutual, dx: Float, dy: Float)

    fun resizeMutual(mutual: Mutual, dw: Int, dh: Int) {

    }


    fun redeclare() {
        if (flag.flagHas(REDECLARE_SHL)) return
        flag = flag.flag(REDECLARE_SHL)
        printStack("ggggggggggggx")
        runMainPost {
            declare(containerLayout != null)
            flag = flag.flagClear(REDECLARE_SHL)
            MutualCenter.relayout()
        }

    }

    fun declare(withSync: Boolean = false) {
        traceBegin("declare_$this")
        if (withSync) {
            MutualCenter.declaring(this) {
                onDeclare()
            }
        } else {
            onDeclare()
        }
        traceEnd()
    }

    abstract fun onDeclare()

    override fun measureLayout(
        size: XY,
        fitContent: Boolean
    ) {
//        if (size.x == spaceInfo.w && size.y == spaceInfo.h) return

        this.fitContent = fitContent
        spaceInfo.setWH(size.x, size.y)

        // 布局contents的位置,如果有relyLayout的layout,要进行布局
        traceBegin("layout_$this")
        layout(size, fitContent)
        traceEnd()

        contents.forEach {
            it.mutual.inputFromLayout()
        }

        spaceInfo.setWH(size.x, size.y)

        afterLayout?.invoke(this)
    }

    /**
     * 内部的mutual有relayout
     * 需要重新进行内部布局
     */
    fun relayoutContent() {
//
//        if(relyContent){
//            //如果relayout发现尺寸更新,需要通知自己的容器layout刷新
//                //中间用containerLayout的size去限制大小
//                containerLayout?.containerSize
//            val selfSizeChanged = true
//            if(selfSizeChanged){
//                relayout()
//            }
//        }
    }

    fun measureSingleMutual(
        limitSize: XY,
        mutual: Mutual,
        spaceInfo: SpaceInfo
    ) {
        traceBegin("measureSingleMutual_$this")
        tempSize.set(limitSize)
        MutualCenter.layouting(mutual) {
            // layout  有确定尺寸,所以在sizeScope里必须进行测量布局
            sizeScope.singleRun(
                tempSize,
                mutual::measureLayout,
                mutual.sizeCall,
                mutual is Layout
            )
        }
        spaceInfo.setWH(tempSize.x, tempSize.y)
        traceEnd()
    }

    /**
     * fitcontent时 layout要自己完成size设置
     * 否则别动这个值
     */
    abstract fun layout(
        size: XY,
        fitContent: Boolean
    )

    override fun onDraw(frame: Frame) {
        // TODO: 2022/1/7  出现了fitLongest这类的容器尺寸和布局不同,要进行缩放
//        d("xxxxxx  $this  $contents")
        layoutScope.baseContents.forEach { (mutual, _, spaceInfo) ->
            frame.limit(spaceInfo.getOrbitInfo(), mutual is Layout, mutual) {
                mutual.draw(frame)
            }
        }
        if (debug != 0) {
//            frame.mainPaint.color = debug.alpha(0.3f)
//            frame.drawRect()
        }
    }

    fun insert(mutual: Mutual) {

    }

    fun remove(mutual: Mutual) {

    }

    override var debug = 0

    override fun debug(color: Int) = apply {
        this.debug = color
    }

    protected var valueCache: HashMap<Int, MutualSync<*>>? = null

    fun <T> cacheValue(
        key: Int,
        value: T,
        withAnim: Boolean = true
    ): MutualSync<T> {
        valueCache ?: let {
            valueCache = HashMap()
        }
        val valueCache = valueCache!!
        return (valueCache[key] as? MutualSync<T>) ?: let {
            MutualSync(value, withAnim).apply {
                valueCache[key] = this
            }
        }
    }

    var afterLayout: ((Layout) -> Unit)? = null

    /**
     * 在这里做布局完成后,获取尺寸位置的数据
     * 如果这里再对布局发生更改,将直接报错
     */
    fun afterLayout(afterLayout: (Layout) -> Unit) = apply {
        this.afterLayout = afterLayout
    }

    fun allDo(doo: Mutual.() -> Unit) = apply {

    }

    open fun packMutual(mutual: Mutual, declareKey: Int): BaseMutualPackage {
        return BaseMutualPackage(mutual, declareKey)
    }

}