package com.mo.mutual.behavior

import com.mo.mutual.Mutual

class BehaviorScope(val mutual: Mutual) {

    val list = ArrayList<Behavior>()

    fun bind(behavior: Behavior) {
        list.add(behavior)
        behavior.bind(mutual)
    }

}