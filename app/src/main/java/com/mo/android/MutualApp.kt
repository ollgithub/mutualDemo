package com.mo.android

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context
import com.mo.android.assist.WindowServer
import com.mo.base.Start
import com.mo.consuming
import com.mo.core.Core
import com.mo.d
import com.mo.runCatchingPrint
import dalvik.system.DexFile
import dalvik.system.PathClassLoader


class MutualApp : Application() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var CONTEXT: Context
    }

    override fun attachBaseContext(base: Context) {
        CONTEXT = this
        super.attachBaseContext(base)
        scanStartEntity()
        WindowServer.initialization()
    }

    private fun scanStartEntity() {
        runCatchingPrint {
            val classLoader = Thread.currentThread().contextClassLoader as PathClassLoader
            val dex = DexFile(this.packageCodePath)
            val entries = dex.entries()
            while (entries.hasMoreElements()) {
                val entryName = entries.nextElement()
                if (!entryName.startsWith(this.packageName)) continue
                var entryClass: Class<*> = this.javaClass
                runCatchingPrint {
                    entryClass = Class.forName(entryName, true, classLoader)
                }
                for (i in entryClass.annotations) {
                    if (i?.annotationClass?.qualifiedName == Start::class.java.canonicalName) {
                        Core.startEntityClass = entryClass
                        return
                    }
                }
            }
        }
    }

}