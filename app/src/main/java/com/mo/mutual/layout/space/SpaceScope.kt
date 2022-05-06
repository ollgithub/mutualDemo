package com.mo.mutual.layout.space

import com.mo.base.SpaceInfo
import com.mo.index
import com.mo.mutual.Mutual
import com.mo.mutual.layout.Layout
import com.mo.mutual.layout.BaseMutualPackage
import com.mo.mutual.layout.LayoutScope

class SpaceScope(layout: Layout) : LayoutScope(layout) {

    val positionScope = SpacePositionScope()


    val contents: ArrayList<SpaceMutualPackage>
        get() = baseContents as ArrayList<SpaceMutualPackage>

    data class RelyNode(
        val self: SpaceMutualPackage,
        val next: RelyNode? = null
    )

    var idMap: HashMap<Int, SpaceMutualPackage>? = null

    val relyContents = ArrayList<SpaceMutualPackage>()

    fun Mutual.position(
        call: SpacePositionScope.() -> Unit
    ) = apply {
        val mutualP: SpaceMutualPackage?
        val last = contents[declareCursor.index]
        mutualP = if (last.mutual == this) {
            last
        } else {
            contents.find { it.mutual == this }
        }
        mutualP ?: return@apply

        mutualP.run {
            positionCall = call
            positionScope.singleRun(mutual, call)
            val newRule = positionScope.spaceRule
            if (newRule.left != null
                || newRule.right != null
                || newRule.top != null
                || newRule.bottom != null
            ) {
                rule = newRule
            }

// relyLayout链式
            // 构建idmap
            // 链表
            // 排序
        }
    }

    fun singleRun(
        layout: Layout,
        declareFunction: (SpaceScope.() -> Unit)?
    ) {
        baseSingleRun(
            layout = layout
        ) {
            declareFunction?.invoke(this)
        }
        contents.forEach { property ->
            if (property.mutual.id > 0) {
                idMap ?: let {
                    idMap = HashMap<Int, SpaceMutualPackage>().apply {
                        val layoutPac =
                            SpaceMutualPackage(mutual = layout, spaceInfo = layout.spaceInfo)
                        set(0, layoutPac)
                        if (layout.id > 0) {
                            set(layout.id, layoutPac)
                        }
                    }
                }
                idMap?.set(property.mutual.id, property)
            }
        }

        idMap?.let { map ->
            contents.forEach { property ->
                property.rule?.let { rule ->
                    rule.left?.id?.let { id -> addNode(map, id, property) }
                    rule.right?.id?.let { id -> addNode(map, id, property) }
                    rule.top?.id?.let { id -> addNode(map, id, property) }
                    rule.bottom?.id?.let { id -> addNode(map, id, property) }
                }
            }
        }
        contents.forEach { property ->
            sort(property)
        }
    }

    private fun sort(property: SpaceMutualPackage) {
        property.relyIt?.let { rely ->
            var next: RelyNode? = rely
            while (next != null) {
                if (relyContents.contains(next.self).not()) {
                    sort(next.self)
                }
                next = rely.next
            }
            if (relyContents.contains(property).not()) {
                relyContents.add(property)
            }
        } ?: let {
            relyContents.add(property)
        }
    }

    private fun addNode(
        map: HashMap<Int, SpaceMutualPackage>,
        id: Int,
        thisProperty: SpaceMutualPackage
    ) {
        val lastNode = map[id]?.relyIt
        map[id]?.let { target ->
            target.relyIt = RelyNode(thisProperty, lastNode)
            if (target.mutual.relyLayout) {
                thisProperty.mutual.relyLayout = true
            }
        }
    }

}