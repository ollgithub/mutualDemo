package com.mo.mutual.layout.space

import com.mo.base.SpaceInfo
import com.mo.mutual.Mutual
import com.mo.mutual.OrbitSpaceInfo
import com.mo.mutual.layout.BaseMutualPackage

class SpaceMutualPackage(
    mutual: Mutual,
    declareKey: Int = -1,
    spaceInfo: OrbitSpaceInfo = OrbitSpaceInfo(),
    var positionCall: (SpacePositionScope.() -> Unit)? = null,
    var rule: SpaceRule? = null,
    var relyIt: SpaceScope.RelyNode? = null
) : BaseMutualPackage(mutual, declareKey, spaceInfo) {

    operator fun component4() = positionCall

    operator fun component5() = rule

    operator fun component6() = relyIt

}