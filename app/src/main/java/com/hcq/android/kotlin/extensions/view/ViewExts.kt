package com.hcq.android.kotlin.extensions.view

import android.graphics.Outline
import android.graphics.Rect
import android.view.View
import android.view.View.NO_ID
import android.view.ViewGroup
import android.view.ViewOutlineProvider
import android.view.inputmethod.InputMethodManager
import androidx.annotation.Px
import androidx.core.view.ViewCompat.generateViewId
import androidx.core.view.marginBottom
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import androidx.core.view.updateLayoutParams
import com.hcq.android.kotlin.extensions.content.getInputMethodManager
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds

private const val FAST_CLICK_INTERVAL = 300L

fun View.setSafeOnClickListener(
    clickInterval: Long = FAST_CLICK_INTERVAL,
    listener: View.OnClickListener,
) {
    setOnClickListener { v ->
        if (!v.isFastClick(clickInterval)) {
            listener.onClick(v)
        }
    }
}

fun View.isFastClick(clickInterval: Long = FAST_CLICK_INTERVAL): Boolean {
    var result = false
    val now = System.currentTimeMillis()
    val tagId = if (id == NO_ID) {
        generateViewId()
    } else {
        id
    }
    val lastClickTime = getTag(tagId).toString().toLongOrNull() ?: 0L
    if (now - lastClickTime < clickInterval) {
        result = true
    }
    setTag(tagId, now)
    return result
}

fun View.hideKeyboard() {
    val imm = context.getInputMethodManager() ?: return

    try {
        imm.hideSoftInputFromWindow(windowToken, 0)
    } catch (e: Exception) {
        @Suppress("DEPRECATION")
        imm.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, InputMethodManager.HIDE_NOT_ALWAYS)
    }
}

fun View.asRound(radius: Int) {
    outlineProvider = object : ViewOutlineProvider() {
        override fun getOutline(view: View, outline: Outline) {
            val rect = Rect()
            view.getDrawingRect(rect)
            outline.setRoundRect(rect, radius.toFloat())
        }
    }
    clipToOutline = true
}


fun View.setMargin(
    @Px start: Int = marginStart,
    @Px top: Int = marginTop,
    @Px end: Int = marginStart,
    @Px bottom: Int = marginBottom,
) {
    updateLayoutParams<ViewGroup.MarginLayoutParams> {
        marginStart = start
        topMargin = top
        marginEnd = end
        bottomMargin = bottom
    }
}

fun View.isInScreen(): Boolean {
    val location = IntArray(2)
    getLocationOnScreen(location)
    return location[0] >= 0 && location[1] >= 0
}