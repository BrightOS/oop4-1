package ru.brightos.oop41.model

class NodeModel<E>(
    var value: E
) {
    var next: NodeModel<E>? = null
    var previous: NodeModel<E>? = null

    override fun toString(): String {
        return "$value"
    }
}