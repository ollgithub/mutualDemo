package com.mo.mutual.layout.collage

import android.graphics.Point
import android.graphics.Rect
import androidx.core.graphics.component1
import androidx.core.graphics.component2
import androidx.core.graphics.component3
import androidx.core.graphics.component4
import com.mo.base.ListUpdateInfo
import com.mo.base.SpaceInfo
import com.mo.base.XY
import com.mo.mutual.Frame
import com.mo.mutual.Mutual
import com.mo.mutual.layout.Layout
import com.mo.mutual.layout.LayoutScope
import com.mo.mutual.layout.BaseMutualPackage
import java.util.*
import kotlin.math.max

//
//fun <T> Collage(
//    list: List<T>,
//    updateInfo: List<ListUpdateInfo>? = null,
//    declareFunction: CollageScope.(T) -> Unit
//) = CollageLayout(
//    ListLinker(
//        count = list.size,
//        getItemCall = {
//            list[it]
//        },
//        updateInfo = updateInfo
//    ), declareFunction
//)

fun <T> LayoutScope.Collage(
    list: List<T>,
    updateInfo: List<ListUpdateInfo>? = null,
    declareFunction: CollageScope.(T) -> Unit
): CollageLayout<T> = declare {
    this.listLinker = ListLinker(
        count = list.size,
        getItemCall = {
            list[it]
        },
        updateInfo = updateInfo
    )

    this.declareFunction = declareFunction
}

class ListLinker<T>(
    var count: Int = 0,
    var getItemCall: ((Int) -> T),
    var updateInfo: List<ListUpdateInfo>? = null
)

class CollageLayout<T>(
    initListLinker: ListLinker<T>? = null,
    initDeclare: (CollageScope.(T) -> Unit)? = null,
) : Layout() {

    var declareFunction: (CollageScope.(T) -> Unit)? = initDeclare
        set(value) {
            field = value
            declare(containerLayout != null)
        }

    var listLinker: ListLinker<T>? = initListLinker

    companion object {

    }

    override val layoutScope = CollageScope(this)

    override fun moveMutual(mutual: Mutual, dx: Float, dy: Float) {
        TODO("Not yet implemented")
    }

    init {
        declare(containerLayout != null)
    }


    override fun onDeclare() {

    }

    val contentss = ArrayList<BaseMutualPackage>()

    override fun layout(size: XY, fitContent: Boolean) {
        val listLinker = listLinker ?: return

        var index = -1
        val anchors = ArrayList<Point>()
        anchors.add(Point(0, 0))
        val activeRect = ArrayList<Rect>()

        while (index + 1 < listLinker.count) {
            index++
            val item = listLinker.getItemCall(index)
            if (contentss.getOrNull(index) == null) {
                layoutScope.singleRun(item, this, declareFunction)
                contentss.add(index, layoutScope.baseContents[index])
            }
            val (mutual, _, itemSpaceInfo) = contentss.getOrNull(index) ?: continue
            measureSingleMutual(size, mutual, itemSpaceInfo)


            fun calculate(spaceInfo: SpaceInfo) {

                fun findNewAreaWithAnchor(spaceInfo: SpaceInfo): Rect? {
                    var right = 0
                    var bottom = 0
                    val containerX = spaceInfo.w - 1
                    // 按锚点测试是否能布局成功
                    val anchorsIterator = anchors.iterator()
                    while (anchorsIterator.hasNext()) {
                        val anchorPoint = anchorsIterator.next()
                        right = anchorPoint.x + spaceInfo.w - 1
                        bottom = anchorPoint.y + spaceInfo.h - 1
                        // 超过容器尺寸，过
                        if (right > containerX) continue


                        val newRect = Rect(anchorPoint.x, anchorPoint.y, right, bottom)
                        var continueAnchor = false
                        // 与已有布局冲突，过
                        for (j in activeRect) {
                            if (Rect.intersects(newRect, j)) {
                                continueAnchor = true
                                break
                            }
                        }
                        if (continueAnchor) continue
                        val ans = Rect(anchorPoint.x, anchorPoint.y, right, bottom)

                        anchorsIterator.remove()
                        return ans
                    }
                    return null
                }

                // 按锚点测试是否能布局成功
                val (x, y, right, bottom) = findNewAreaWithAnchor(spaceInfo) ?: return
                spaceInfo.setXY(x, y)

                // 寻找新锚点
                var newAnchorLBX = -1
                var newAnchorRTY = -1
                activeRect.forEach { rect ->
                    if (rect.bottom >= bottom + 1) {
                        newAnchorLBX = max(newAnchorLBX, rect.right)
                    } else if (rect.right >= right + 1) {
                        newAnchorRTY = max(newAnchorRTY, rect.bottom)
                    }
                }

                // 补充活动区域
                spaceInfo.apply {
                    activeRect.add(Rect(x, y, right, bottom))
                }

                val newRTPoint = Point(right + 1, newAnchorRTY + 1)
                val newLBPoint = Point(newAnchorLBX + 1, bottom + 1)
                var newRTPointRepeat = false
                var newLBPointRepeat = false

                // 小于新的两个锚点的锚点成为死锚点
                val anchorIterator = anchors.iterator()
                while (anchorIterator.hasNext()) {
                    val anchorPoint = anchorIterator.next()
                    // 删除不能再作为锚点的点
                    val del = (anchorPoint.x in x..right) && (anchorPoint.y <= bottom)
                    if (del) anchorIterator.remove()
                    newRTPointRepeat = newRTPointRepeat or (newRTPoint == anchorPoint)
                    newLBPointRepeat = newLBPointRepeat or (newLBPoint == anchorPoint)
                }

                if (right + 1 < spaceInfo.w && !newRTPointRepeat) anchors.add(newRTPoint)
                if (
//                    bottom + 1 < containerSize.y &&
                    !newLBPointRepeat) anchors.add(newLBPoint)

                anchors.sortBy { it.y * 10000 + it.x }

                val rectIterator = activeRect.iterator()
                while (rectIterator.hasNext()) {
                    val rect = rectIterator.next()
                    val del = (rect.right <= newRTPoint.x && rect.bottom <= newRTPoint.y)
                            || (rect.right <= newLBPoint.x && rect.bottom <= newLBPoint.y)
                    if (del) rectIterator.remove()
                }

            }
//            d("ooooo ${anchors}")

            calculate(itemSpaceInfo)

//            e("layoutttt $itemSpaceInfo")
        }
        layoutScope.endDeclare()
    }

    override fun onDraw(frame: Frame) {
        contentss.forEach { (mutual, _, spaceInfo) ->
            frame.limit(spaceInfo, mutual is Layout) {
                mutual.draw(frame)
            }
        }
    }

}
