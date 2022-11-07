package ru.brightos.oop41.utils

import android.content.res.Resources
import android.util.TypedValue
import ru.brightos.oop41.data.ExtendedList

fun <T> extendedListOf(vararg elements: T): ExtendedList<T> =
    if (elements.size == 0) ExtendedList() else ExtendedList(elements.toList())

val Number.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this.toFloat(),
        Resources.getSystem().displayMetrics
    )