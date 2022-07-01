package com.cdsap.bagan.squaretheblack


class CircularQueue(val size: Int) {
    var queue = Array(size){-1}
    var front = -1
    var rear = -1

    fun isFull(): Boolean {
        return false
    }

    fun isEmpty(): Boolean {
        return false
    }

    fun enqueue(value: Int) {
        if (isFull()) {
            println("queue full")
        }
        if (front == -1) {
            front = 0
        }
        rear = (rear + 1) % size
        queue[rear] = value
    }

    fun dequeue(): Int {
        if (isEmpty()) {
            return -1
        }
        val element = queue[front]
        front = (front + 1) % size
        return element
    }
}

fun main() {
    


}


//    fun enqueue(element: Int) {
//        // check if it's full
//        if (isFull())
//         {
//            println("queue full")
//        } else {
//            if (front == -1)
//                front = 0;
//            rear = (rear + 1) % size;
//            queue[rear] = element;
//        }
//
//    }
//
//    fun dequeue(): Int? {
//        if (isEmptu()) {
//            return null
//        } else {
//            val element = queue[front]
//            if (front == rear) {
//                front = -1
//                rear = -1
//            } else {
//                front = (front + 1) % size
//            }
//            return element
//        }
//
//    }

}