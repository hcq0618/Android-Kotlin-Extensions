package com.hcq.android.kotlin.extensions.content

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager

inline val Context.landscape: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

inline val Context.portrait: Boolean
    get() = resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT

inline val Context.rtl: Boolean
    get() = resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL

inline val Context.rtlControlChar: Char
    get() = if (rtl) {
        '\u200F'
    } else {
        '\u200E'
    }

inline val Activity.contentView: View?
    get() = findViewById<ViewGroup?>(android.R.id.content)?.getChildAt(0)

inline fun <reified T : Activity> Context.startActivity(vararg params: Pair<Any, Any?>) =
    startActivity(intentFor<T>(*params))


fun Context.getInputMethodManager() =
    getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager

fun Context.isKeyboardShowing(): Boolean {
    val imm = getInputMethodManager()
    return imm?.isActive == true
}


