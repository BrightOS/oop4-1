package ru.brightos.oop41.data

import ru.brightos.oop41.model.NodeModel

class ELWorker<E>(val list: ExtendedList<E>) {
    var position = 0

    private var _current: NodeModel<E>? = list.first
    val current: E
        get() {
            return _current!!.value
        }

    fun add(element: E) {
        list.add(element)
    }

    fun removeAt(index: Int) {
        list.removeAt(index)
    }

    fun remove(element: E) {
        list.remove(element)
    }

    fun remove() {
        list.removeAt(position)
        position--
        next()
    }

    fun first() {
        position = 0
        _current = list.first
    }

    fun next() {
        position++
        _current = _current?.next
    }

    fun last() {
        position = list.size - 1
        _current = list.last
    }

    fun eof(): Boolean {
        return _current == null
    }
}