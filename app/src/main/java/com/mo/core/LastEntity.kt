package com.mo.core

import com.mo.base.Changed
import com.mo.mutual.Mutual
import java.util.concurrent.CopyOnWriteArrayList

open class LastEntity {


    var living: Boolean by Changed(false) {
        doAllEntity { living = it }
        onLive(it)
    }

    var waking: Boolean by Changed(false) {
        doAllEntity { waking = it }
        onWake(it)
    }

    var showing: Boolean by Changed(false) {
        doAllEntity { showing = it }
        onShow(it)
    }

    private var entities: CopyOnWriteArrayList<LastEntity>? = null

    open fun onLive(living: Boolean) {}
    open fun onWake(waking: Boolean) {}
    open fun onShow(showing: Boolean) {}

    open val mutual: Mutual? = null

    fun addEntity(entity: LastEntity) {
        entities ?: let {
            entities = CopyOnWriteArrayList<LastEntity>()
        }
        entities?.add(entity)
    }

    fun doAllEntity(function: LastEntity.() -> Unit) {
        entities?.let {
            for (i in it) {
                function(i)
            }
        }
    }

    fun attach(base: LastEntity) = apply {
        base.addEntity(this)
        waking = base.waking
        showing = base.showing
        living = base.living
    }

    fun start() {
//        Core.startEntity(this)
    }

    fun LastEntity.entity(function: LastEntity.() -> Unit) {

    }

}