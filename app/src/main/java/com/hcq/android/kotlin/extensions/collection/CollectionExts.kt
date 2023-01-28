package com.hcq.android.kotlin.extensions.collection

fun <T> List<T>.asMutableList(): MutableList<T> {
    return if (this is MutableList) this else this.toMutableList()
}

fun <T> Set<T>.asMutableSet(): MutableSet<T> {
    return if (this is MutableSet) return this else this.toMutableSet()
}

fun <K, V> Map<K, V>.getKey(target: V): K? {
    return keys.firstOrNull { target == get(it) }
}

fun <T> MutableList<T>.move(item: T, newIndex: Int) {
    val currentIndex = indexOf(item)
    if (currentIndex < 0) return
    removeAt(currentIndex)
    add(newIndex, item)
}