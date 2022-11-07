package ru.brightos.oop41.data

import ru.brightos.oop41.model.NodeModel
import java.lang.IndexOutOfBoundsException

class ExtendedList<E> {
    constructor()

    constructor(elements: List<E>) {
        elements.forEach {
            add(it)
        }
    }

    constructor(other: ExtendedList<E>) {
        other.forEach {
            add(it)
        }
    }

    var first: NodeModel<E>? = null
    var last: NodeModel<E>? = null

    val isEmpty: Boolean
        get() = size < 1

    var size: Int = 0

    fun add(element: E) {
        val newNodeModel = NodeModel(element)

        if (last != null) {
            newNodeModel.previous = last
            last?.next = newNodeModel
        } else
            first = newNodeModel

        last = newNodeModel
        size++
    }

    fun add(index: Int, element: E) {
        if (index >= size)
            return add(element)

        if (index < 0)
            throw IndexOutOfBoundsException()

        val previous = if (index > 0) getNodeModel(index - 1) else null
        val next = if (previous != null) previous.next else first
        if (index == 0)
            first = NodeModel(element).apply {
                this.next = next
            }
        val newNodeModel = NodeModel(element).apply {
            this.previous = previous
            this.next = next
        }
        size++
        previous?.next = newNodeModel
        next?.previous = newNodeModel
    }

    private fun getNodeModel(index: Int): NodeModel<E>? {
        if (index < 0 || index >= size)
            throw IndexOutOfBoundsException()

        var node: NodeModel<E>?

        if (index < size / 2) {
            node = first
            var _index = 0
            while (_index++ != index) {
                node = node?.next
            }
        } else {
            node = last
            var _index = size - 1
            while (_index-- != index) {
                node = node?.previous
            }
        }

        return node
    }

    operator fun get(index: Int): E = getNodeModel(index)!!.value

    fun indexOfFirst(predicate: (E) -> Boolean): Int {
        var node = first
        var index = 0

        while (node?.value?.let { predicate(it) } == false && index < size) {
            node = node.next
            index++
        }

        return if (index == size) -1 else index
    }

    fun count(predicate: (E) -> Boolean): Int {
        var result = 0
        forEach {
            if (predicate(it))
                result++
        }
        return result
    }

    fun clear() {
        first = null
        last = null
        size = 0
    }

    fun removeAt(index: Int) {
        if (index < 0 || index >= size)
            throw IndexOutOfBoundsException()

        if (index == 0)
            return removeFirst()

        if (index == size - 1)
            return removeLast()

        var node: NodeModel<E>?

        if (index < size / 2) {
            node = first
            var _index = 0
            while (_index++ != index) {
                node = node?.next
            }
        } else {
            node = last
            var _index = size - 1
            while (_index-- != index) {
                node = node?.previous
            }
        }

//        println("${node?.previous?.next?.value} ${node?.next?.value} ${node?.next?.previous?.value} ${node?.previous?.value}")
        node?.previous?.next = node?.next
        if (node?.previous?.next == null)
            first = node?.next
        node?.next?.previous = node?.previous
//        println("${node?.previous?.next?.value} ${node?.next?.value} ${node?.next?.previous?.value} ${node?.previous?.value}")
        size--
    }

    fun removeFirst() {
        first = first?.next.apply {
            this?.previous = null
        }
        size--
        if (size == 0)
            last = null
    }

    fun removeLast() {
        last = last?.previous.apply {
            this?.next = null
        }
        size--
        if (size == 0)
            first = null
    }

    fun remove(element: E) {
        var node = first

        if (node?.value == element) {
            first = node?.next
            size--
            return
        }

        while (node?.next != element && node?.next != null)
            node = node.next

        if (node?.next != null) {
            node.next = node.next?.next
            size--
        }
    }

    fun doOnWorker(e: ELWorker<E>.() -> Unit) {
        if (size > 0)
            e(ELWorker(this))
    }

    fun forEach(action: (E) -> Unit) {
        if (size > 0)
            repeat(size) {
                action(get(it))
            }
    }

    override fun toString(): String {
        var result = "["
        doOnWorker {
            while (!eof()) {
                result += "$current, "
                this.next()
            }
        }
        if (result.length > 1)
            result = result.dropLast(2)
        result += "]"
        return result
    }
}