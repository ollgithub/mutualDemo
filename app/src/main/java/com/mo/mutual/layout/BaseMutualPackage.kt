package com.mo.mutual.layout

import com.mo.base.SpaceInfo
import com.mo.mutual.Mutual
import com.mo.mutual.OrbitSpaceInfo
import com.mo.mutual.layout.space.SpacePositionScope
import com.mo.mutual.layout.space.SpaceRule
import com.mo.mutual.layout.space.SpaceScope

open class BaseMutualPackage(
    val mutual: Mutual,
    val declareKey: Int = -1,
    val spaceInfo: OrbitSpaceInfo = OrbitSpaceInfo()
) {

    operator fun component1() = mutual

    operator fun component2() = declareKey

    operator fun component3() = spaceInfo
}