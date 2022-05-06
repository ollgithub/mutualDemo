package com.mo.android.assist

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileDescriptor
import java.io.FileInputStream
import java.io.InputStream

class FileCompatible {

    companion object {
        private val FILE_PREFIX = "file://"
        private val ASSET_PREFIX = "$FILE_PREFIX/android_asset/"
        private val RESOURCE_PREFIX = ContentResolver.SCHEME_ANDROID_RESOURCE + "://"
        private val Empty = FileCompatible()
    }

    private val context: Context
        get() = CONTEXT

    var path: String? = null
        private set

    var uri: Uri? = null
        private set

    var file: File? = null
        private set

    fun path(path: String): FileCompatible {
        this.path = path
        return this
    }

    fun uri(uri: Uri): FileCompatible {
        this.uri = uri
        return this
    }

    fun file(file: File): FileCompatible {
        this.file = file
        return this
    }

    val exists: Boolean
        get() {
            file?.let {
                return it.exists()
            }
            path?.let {
                file = File(it)
                return file!!.exists()
            }
            uri?.let {
                return context.contentResolver.getType(it) != null
            }
            return false
        }


    val inputStream: InputStream?
        get() {
            return file?.let {
                it.inputStream()
            } ?: path?.let {
                File(it).inputStream()
            } ?: uri?.let {
                context.contentResolver.openInputStream(it)
            }
        }

    val fileInputStream: FileInputStream?
        get() {
            return file?.let {
                FileInputStream(it)
            } ?: path?.let {
                FileInputStream(File(it))
            }
        }

    fun <T> fd(mode: String = "r", function: (FileDescriptor) -> T): T? {
        val fileInputStream = file?.let {
            FileInputStream(it)
        } ?: path?.let {
            FileInputStream(File(it))
        }
        fileInputStream?.use {
            return function.invoke(it.fd)
        } ?: let {
            uri?.let { uri ->
                context.contentResolver.openFileDescriptor(uri, mode)?.fileDescriptor?.let {
                    return function.invoke(it)
                }
            }
        }
        return null
    }

    fun openUri(): Uri? {
        return uri ?: let {
            file?.let {
                Uri.fromFile(it)
            } ?: path?.let {
                Uri.fromFile(File(it))
            }
        }
    }

    fun openFile(): File? {
        return file?.let {
            it
        } ?: path?.let {
            file = File(it)
            file
        }
    }

}