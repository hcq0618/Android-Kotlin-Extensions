package com.hcq.android.kotlin.extensions.content

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import java.io.Serializable

fun Intent.clearTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK) }

fun Intent.clearTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP) }

fun Intent.excludeFromRecents(): Intent =
    apply { addFlags(Intent.FLAG_ACTIVITY_EXCLUDE_FROM_RECENTS) }

fun Intent.multipleTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK) }

fun Intent.newTask(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NEW_TASK) }

fun Intent.noAnimation(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION) }

fun Intent.noHistory(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY) }

fun Intent.singleTop(): Intent = apply { addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP) }

inline fun <reified T : Any> Context.intentFor(vararg params: Pair<Any, Any?>): Intent {
    val intent = Intent(this, T::class.java)
    params.forEach {
        val name = when (it.first) {
            is String -> it.first as String
            is Enum<*> -> (it.first as Enum<*>).name
            else -> throw AssertionError("Intent name has wrong type ${it.first.javaClass.name}")
        }
        when (val value = it.second) {
            null -> intent.putExtra(name, null as Serializable?)
            is Int -> intent.putExtra(name, value)
            is Long -> intent.putExtra(name, value)
            is String -> intent.putExtra(name, value)
            is CharSequence -> intent.putExtra(name, value)
            is Float -> intent.putExtra(name, value)
            is Double -> intent.putExtra(name, value)
            is Char -> intent.putExtra(name, value)
            is Short -> intent.putExtra(name, value)
            is Boolean -> intent.putExtra(name, value)
            is Array<*> -> when {
                value.isArrayOf<CharSequence>() -> intent.putExtra(name, value)
                value.isArrayOf<String>() -> intent.putExtra(name, value)
                value.isArrayOf<Parcelable>() -> intent.putExtra(name, value)
                else -> throw AssertionError("Intent extra $name has wrong type ${value.javaClass.name}")
            }
            is IntArray -> intent.putExtra(name, value)
            is LongArray -> intent.putExtra(name, value)
            is FloatArray -> intent.putExtra(name, value)
            is DoubleArray -> intent.putExtra(name, value)
            is CharArray -> intent.putExtra(name, value)
            is ShortArray -> intent.putExtra(name, value)
            is BooleanArray -> intent.putExtra(name, value)
            is Serializable -> intent.putExtra(name, value)
            is Bundle -> intent.putExtra(name, value)
            is Parcelable -> intent.putExtra(name, value)
            else -> throw AssertionError("Intent extra $name has wrong type ${value.javaClass.name}")
        }
        return@forEach
    }
    return intent
}

fun Intent.isValid(context: Context): Boolean {
    return context.packageManager?.resolveActivity(this, 0) != null
}