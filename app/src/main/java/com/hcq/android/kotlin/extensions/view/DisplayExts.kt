package com.hcq.android.kotlin.extensions.view

import android.content.Context
import android.hardware.display.DisplayManager
import android.os.Build
import android.view.Display
import android.view.WindowManager
import androidx.annotation.Px

fun Context.getDisplayManager() = getSystemService(Context.DISPLAY_SERVICE) as? DisplayManager

fun Context.getWindowManager() = getSystemService(Context.WINDOW_SERVICE) as? WindowManager

fun Context.getDefaultDisplay(): Display? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
        // This one (context) may or may not have a display associated with it, due to it being
        // an application context
        val defaultDisplay = getDisplayManager()?.getDisplay(Display.DEFAULT_DISPLAY)
        val displayContext = defaultDisplay?.let { createDisplayContext(it) }
        displayContext?.display
    } else {
        getWindowManager()?.defaultDisplay
    }
}

inline val Context.screenDensity: Float get() = resources.displayMetrics.density

inline val Context.scaledDensity: Float get() = resources.displayMetrics.scaledDensity

inline val Context.screenWidth: Int @Px get() = resources.displayMetrics.widthPixels

inline val Context.screenHeight: Int @Px get() = resources.displayMetrics.heightPixels

inline val Context.screenWidthDp: Int get() = (screenWidth / screenDensity).toInt()

inline val Context.screenHeightDp: Int get() = (screenHeight / screenDensity).toInt()

@Px
fun Number.dp(context: Context): Int {
    return (toFloat() * context.screenDensity + 0.5f).toInt()
}

@Px
fun Number.dpF(context: Context): Float {
    return toFloat() * context.screenDensity
}

@Px
fun Number.sp(context: Context): Int {
    return (toFloat() * context.scaledDensity + 0.5f).toInt()
}

fun Number.px(context: Context): Int {
    return (toFloat() / context.screenDensity).toInt()
}