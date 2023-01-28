package com.hcq.android.kotlin.extensions.view

import android.widget.TextView
import androidx.annotation.DrawableRes

fun TextView.setDrawable(
    @DrawableRes start: Int = 0,
    @DrawableRes top: Int = 0,
    @DrawableRes end: Int = 0,
    @DrawableRes bottom: Int = 0
) {
    setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)
}