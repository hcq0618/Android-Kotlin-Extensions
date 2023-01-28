package com.hcq.android.kotlin.extensions.view

import android.graphics.Point
import android.os.Build
import android.view.ViewGroup
import android.view.Window
import android.view.WindowInsets
import android.view.WindowManager
import androidx.core.graphics.Insets
import androidx.core.view.*

fun Window.enterImmersiveMode(hideStatusBar: Boolean = true) {
    WindowCompat.getInsetsController(this, decorView).run {
        hide(
            if (hideStatusBar) {
                WindowInsetsCompat.Type.systemBars()
            } else {
                WindowInsetsCompat.Type.navigationBars()
            }
        )
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun Window.exitImmersiveMode() {
    WindowCompat.getInsetsController(this, decorView).run {
        show(WindowInsetsCompat.Type.systemBars())
        systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_BARS_BY_SWIPE
    }
}

fun Window.fullScreen(updateInsets: Boolean = true) {
    WindowCompat.setDecorFitsSystemWindows(this, false)

    if (updateInsets) {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, windowInsets ->
            val insets = windowInsets.getInsets(WindowInsetsCompat.Type.navigationBars())
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                // resolve navigation bar overlap bottom view
                bottomMargin = insets.bottom
            }

            windowInsets
        }
    }
}

fun Window.nonFullScreen(restoreInsets: Boolean = true) {
    WindowCompat.setDecorFitsSystemWindows(this, true)

    if (restoreInsets) {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content)) { view, windowInsets ->
            view.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                bottomMargin = 0
            }

            windowInsets
        }
    }
}

private fun Window.showBar(@WindowInsetsCompat.Type.InsetsType types: Int) {
    WindowCompat.getInsetsController(this, decorView).show(types)
}

private fun Window.hideBar(@WindowInsetsCompat.Type.InsetsType types: Int) {
    WindowCompat.getInsetsController(this, decorView).hide(types)
}

fun Window.showStatusBar() {
    showBar(WindowInsetsCompat.Type.statusBars())
}

fun Window.hideStatusBar() {
    hideBar(WindowInsetsCompat.Type.statusBars())
}

fun Window.showNavigationBar() {
    showBar(WindowInsetsCompat.Type.navigationBars())
}

fun Window.hideNavigationBar() {
    hideBar(WindowInsetsCompat.Type.navigationBars())
}

fun Window.enableLightStatusBar(enable: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        WindowCompat.getInsetsController(this, decorView).run {
            isAppearanceLightStatusBars = enable
        }
    }
}

fun Window.enableLightNavigationBar(enable: Boolean) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        WindowCompat.getInsetsController(this, decorView).run {
            isAppearanceLightNavigationBars = enable
        }
    }
}

fun Window.enableLightSystemBars(enable: Boolean) {
    enableLightStatusBar(enable)
    enableLightNavigationBar(enable)
}

fun Window.getNavigationBarHeight(): Int {
    return getSystemBarInsets(WindowInsetsCompat.Type.navigationBars())?.bottom ?: 0
}

fun Window.getSystemBarInsets(@WindowInsetsCompat.Type.InsetsType types: Int): Insets? {
    return ViewCompat.getRootWindowInsets(decorView)?.getInsets(types)
}

fun Window.getWidth(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowMetrics = windowManager.currentWindowMetrics
        val insets =
            windowMetrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
        windowMetrics.bounds.width() - insets.left - insets.right
    } else {
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.width
    }
}

fun Window.getHeight(): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val metrics = windowManager.currentWindowMetrics
        val insets = metrics.windowInsets.getInsets(WindowInsets.Type.systemBars())
        metrics.bounds.height() - insets.bottom - insets.top
    } else {
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.height
    }
}

fun WindowManager?.getSize(): Point {
    if (this == null) {
        return Point()
    }

    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        val windowInsets = currentWindowMetrics.windowInsets
        var insets =
            Insets.toCompatInsets(windowInsets.getInsets(WindowInsets.Type.navigationBars()))
        windowInsets.displayCutout?.run {
            insets = Insets.max(
                insets,
                Insets.of(safeInsetLeft, safeInsetTop, safeInsetRight, safeInsetBottom)
            )
        }
        val insetsWidth = insets.right + insets.left
        val insetsHeight = insets.top + insets.bottom
        Point(
            currentWindowMetrics.bounds.width() - insetsWidth,
            currentWindowMetrics.bounds.height() - insetsHeight
        )
    } else {
        Point().apply {
            @Suppress("DEPRECATION")
            defaultDisplay.getRealSize(this)
        }
    }
}