package com.mo.mutual.layout

import com.mo.android.StackTrace
import com.mo.android.assist.traceBegin
import com.mo.android.assist.traceEnd
import com.mo.android.assist.tracing
import com.mo.d
import com.mo.index
import com.mo.mutual.Mutual

/**
 * 只有layout有这玩意
 * 1 负责提供声明环境
 * 2 构建声明列表
 * 3 更新维护mutual列表
 */
abstract class LayoutScope(val layout: Layout) {

    var lastDeclareLine = -1
    var declareCount = 0
    var declareCursor = 0

    var justUpdate = false

    val baseContents = ArrayList<BaseMutualPackage>()

    inline fun <reified T : Mutual> declare(
        externalMutual: Mutual? = null,
        updateFun: T.(Boolean) -> Unit
    ): T {

        if (justUpdate) {
            declareCursor++
            val mutualPack = baseContents[declareCursor.index]
            updateFun(mutualPack.mutual as T,true)
            return mutualPack.mutual
        }

        traceBegin("StackTrace.getCallKey")
        val declarePosition = StackTrace.getCallKey(2)
        traceEnd()
        declareCount = if (lastDeclareLine == declarePosition) declareCount + 1 else 1
        lastDeclareLine = declarePosition
        declareCursor++

        val declareKey: Int = declarePosition shl 17 + declareCount
        while (true) {
            if (declareCursor > baseContents.size) break
            val mutualPack = baseContents[declareCursor.index]
            when {
                mutualPack.declareKey < declareKey -> {
                    mutualPack.spaceInfo
                    // TODO:  
                    baseContents.remove(mutualPack)
                }
                mutualPack.declareKey > declareKey -> {
                    break
                }
                mutualPack.declareKey == declareKey -> {
                    updateFun(mutualPack.mutual as T,true)
                    return mutualPack.mutual
                }
            }
        }
        return addMutual(externalMutual, declareKey, updateFun)
    }

    inline fun <reified T : Mutual> addMutual(
        externalMutual: Mutual? = null,
        declareKey: Int,
        updateFun: T.(Boolean) -> Unit
    ): T {
        val mutual = tracing("Mutual_newInstance") {
            externalMutual as? T ?: T::class.java.newInstance()
        }

        mutual.containerLayout = layout
        mutual.floor = layout.floor + 1

        tracing("updateFun") {
            updateFun(mutual,false)
        }

        tracing("updateFun") {
            baseContents.add(
                declareCursor.index,
                layout.packMutual(
                    mutual,
                    declareKey
                )
            )
        }
        return mutual
    }

    fun endDeclare() {
        repeat(baseContents.size - declareCursor) {
            baseContents.removeAt(declareCursor)
        }
        declareCursor = 0
        lastDeclareLine = -1

    }

    fun baseSingleRun(
        justUpdate: Boolean = false,
        end: Boolean = true,
        layout: Layout,
        runn: () -> Unit
    ) {
        this.justUpdate = justUpdate
        runn()
        if (end) {
            endDeclare()
        }
    }


    protected fun Mutual.bindPositionCall(doo: (BaseMutualPackage) -> Unit) {
        // TODO: 2022/2/26 需验证是否是当前环境的mutual
        kotlin.runCatching {
            val last = baseContents[declareCursor.index]
            if (last.mutual == this) {
                doo.invoke(last)
            } else {
                baseContents.find { it.mutual == this }?.let {
                    doo.invoke(it)
                }
            }
        }.onFailure {
            d("dddddd")
        }

    }

    /**
     * 已有mutual的声明插入
     */
    fun Include(mutual: Mutual): Mutual = declare(mutual) {}

}