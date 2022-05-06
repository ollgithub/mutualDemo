package com.mo.qin

import android.content.Intent
import android.net.Uri
import com.mo.android.assist.ACTIVITY

object FeatureServer {

    fun playVideo(path:String) {
        ACTIVITY.startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_VIEW).apply {
                    setDataAndType(Uri.parse(path), "video/*")
                },
                "video"
            )
        )
    }

    fun setWallpaper(path: String) {
        ACTIVITY.startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_ATTACH_DATA).apply {
                    setDataAndType(Uri.parse(path), "image/*")
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                },
                "wallpaper"
            )
        )
    }

    fun share(uri: Uri) {
        val type = ACTIVITY.contentResolver.getType(uri)
        ACTIVITY.startActivity(
            Intent.createChooser(
                Intent(Intent.ACTION_SEND).apply {
                    putExtra(Intent.EXTRA_STREAM, uri)
                    flags = Intent.FLAG_GRANT_READ_URI_PERMISSION
                    this.type = type
                },
                "share"
            )
        )
    }

}