package ru.brightos.oop41.utils

import android.content.res.Resources
import android.util.TypedValue
import ru.brightos.oop41.data.ExtendedList

fun <T> extendedListOf(vararg elements: T): ExtendedList<T> =
    if (elements.size == 0) ExtendedList() else ExtendedList(elements.toList())

val Number.dp
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), Resources.getSystem().displayMetrics
    )

fun correctDeclensionOfObjects(elementsCount: Int) =
    if (elementsCount % 100 / 10 != 1) when (elementsCount) {
        1 -> "объект"
        in 2..4 -> "объекта"
        else -> "объектов"
    }
    else "объектов"

fun correctDeclensionOfDeleted(deletedCount: Int) =
    if (deletedCount % 100 / 10 != 1 && deletedCount % 10 == 1)
        "Удалён"
    else
        "Удалено"