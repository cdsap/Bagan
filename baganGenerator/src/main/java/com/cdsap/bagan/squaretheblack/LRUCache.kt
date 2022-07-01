package com.cdsap.bagan.squaretheblack

import java.util.*

class LRUCache(val SIZE: Int) {
    val deque: Deque<Int> = LinkedList()
    val elements = hashSetOf<Int>()

    fun get(element: Node): Node? {
        if (elements.contains(element.value)) {
            elements.remove(element.value)
        } else {
            if (deque.size == SIZE) {
                val last = deque.removeLast()
                elements.remove(last)
            }
        }
        deque.push(element.value)
        elements.add(element.value)
        return null

    }

    fun put(value : Int){


    }
}

class Node(val value: Int)