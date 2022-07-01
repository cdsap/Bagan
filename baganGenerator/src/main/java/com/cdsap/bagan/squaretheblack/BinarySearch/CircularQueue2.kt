package com.cdsap.bagan.squaretheblack.BinarySearch

class CircularQueue2(val size: Int) {
    var front = -1
    var rear = -1
    val arr = Array(size) { 0 }
    fun enqueue(value: Int) {
        if (isFull()) {
            println("Full queue")
        }
        if (front == -1) {
            front = 0
        }
        rear = (rear + 1 )% size
        arr[rear] = value

    }

    fun dequeue(): Int {
        if (isEmpty()) {
            println("Empty queue")
        }
        val element = arr[front]
        front = (front + 1) % size
        return element
    }

    fun isEmpty() = false
    fun isFull() = false

    fun log(){
        println("log")
        arr.forEach {
            print(it)
        }
        println("")
        println("front = $front")
        println("Rear = $rear")

    }

}

fun main() {
    val c = CircularQueue2(4)
    c.enqueue(1)
    c.log()
    c.enqueue(2)
    c.log()
    c.dequeue()
    c.log()
    c.dequeue()
    c.log()
    c.enqueue(3)
    c.log()
    c.enqueue(4)
    c.log()
    c.dequeue()
    c.enqueue(5)
    c.enqueue(6)
 c.log()
    c.dequeue()
    c.log()

}