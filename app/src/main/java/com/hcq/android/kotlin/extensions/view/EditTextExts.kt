package com.hcq.android.kotlin.extensions.view

import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.hcq.android.kotlin.extensions.content.getInputMethodManager

fun EditText.showKeyboard() {
    val imm = context.getInputMethodManager() ?: return

    if (!hasFocus()) {
        requestFocus()
    }
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}