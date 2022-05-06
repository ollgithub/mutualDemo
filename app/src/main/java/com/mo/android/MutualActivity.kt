package com.mo.android

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.*
import android.widget.RelativeLayout
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.mo.`false`
import com.mo.core.Core

open class MutualActivity : Activity() {

    companion object {
        @SuppressLint("StaticFieldLeak")
        var ACTIVITY: Activity? = null
    }

    private val permissionList = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.VIBRATE
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        ACTIVITY = this
        configureTheImmersiveStatusBar(window)
        super.onCreate(savedInstanceState)
        setContentView(MutualView(this, Core.space))

//        (window.decorView as ViewGroup).addView(MutualView.textureView,0)
        begin()
    }

    private fun begin() {
        checkPermission()
        Core.begin()
    }

    override fun onResume() {
        super.onResume()
        Core.action(Core.Action.RESUME)
    }

    override fun onStart() {
        super.onStart()
        Core.action(Core.Action.START)
    }

    override fun onStop() {
        super.onStop()
        Core.action(Core.Action.STOP)
    }

    override fun onPause() {
        super.onPause()
        Core.action(Core.Action.PAUSE)
    }

    override fun onDestroy() {
        super.onDestroy()
        Core.action(Core.Action.DESTROY)
        ACTIVITY = null
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Core.action(Core.Action.BACK_PRESS)
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {

    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return super.onKeyDown(keyCode, event)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        when (requestCode) {
            0 -> checkPermission()
        }
    }

    private fun checkPermission(): Boolean {
        return (ContextCompat.checkSelfPermission(
            this,
            permissionList[0]
        ) == PackageManager.PERMISSION_GRANTED) `false` {
            ActivityCompat.requestPermissions(
                this,
                permissionList,
                0
            )
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Core.action(Core.Action.CONFIGURATION)
    }

    private fun configureTheImmersiveStatusBar(window: Window) {
        window.run {
            clearFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
                        or WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
            decorView.systemUiVisibility = (
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
//                            or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                            or View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR
//                            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                    )
            addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            statusBarColor = Color.TRANSPARENT
            navigationBarColor = Color.TRANSPARENT
        }

    }

}