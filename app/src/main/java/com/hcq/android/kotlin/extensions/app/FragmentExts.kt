@file:Suppress("DEPRECATION")

package com.hcq.android.kotlin.extensions.app

import android.app.Fragment

fun androidx.fragment.app.Fragment.finish() {
    parentFragmentManager.beginTransaction()
        .remove(this)
        .commitAllowingStateLoss()
}

fun Fragment.finish() {
    fragmentManager.beginTransaction()
        .remove(this)
        .commitAllowingStateLoss()
}