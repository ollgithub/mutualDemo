package com.mo.core

import android.os.Handler
import android.os.Looper
import android.widget.LinearLayout
import com.mo.mutual.layout.space.Space
import com.mo.runMainPost

object Core {

    var startEntityClass: Class<*>? = null//= QinEntity::class.java

    enum class Action {
        RESUME, PAUSE, START, STOP, DESTROY, BACK_PRESS, CONFIGURATION, WINDOW_FOCUS
    }

    val mainHandler = Handler(Looper.getMainLooper())

    var catchingAll = false

    val space = Space { }

    fun begin() {
        OrbitCore.callNext()
        runMainPost {
            (startEntityClass?.newInstance() as? Entity)?.let { startEntity(it) }
        }
    }

    fun startEntity(entity: Entity) {
        entity.run {

            entity.mutual.let {
                space.declareFunction = {
                    Include(it)
                }
            }
        }
    }

    fun action(action: Action) {

    }

}