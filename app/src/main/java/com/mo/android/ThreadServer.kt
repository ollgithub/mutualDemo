package com.mo.android

import android.os.Handler
import android.os.HandlerThread

object ThreadServer {

    class ThreadHandler(val tag: String) {

        val thread = HandlerThread(tag).apply {
            start()
        }

        val handler = Handler(thread.looper)

        fun post(r: Runnable) {
            if (thread.isAlive) {
                handler.post(r)
            }
        }

        fun post(r: () -> Unit) {
            if (thread.isAlive) {
                handler.post(r)
            }
        }

        fun release() {
            handler.removeCallbacksAndMessages(null)
            thread.quitSafely()
        }
    }

}