package com.cdsap.bagan.squaretheblack

import java.util.*

class BFS {
    fun bfs(noded: Noded?) {
        if (noded != null) {
            val queue: Deque<Noded> = LinkedList()
            queue.push(noded)
            while(!queue.isEmpty()){
                val element = queue.removeLast()
                println("-->${element.value}")
                if(element.nodeLeft != null){
                    println("left not null"+element.nodeLeft)
                    queue.push(element.nodeLeft)
                }
                if(element.nodeRight != null){
                    println("right not null "+element.nodeRight)
                    queue.push(element.nodeRight)
                }
            }
        }
    }
}
/*
     3
  1       2
7   8   4   10

 */

fun main() {
    val node10 = Noded(10, null, null)
    val node4 = Noded(4, null, null)
    val node8 = Noded(8, null, null)
    val node7 = Noded(7, null, null)
    val node2 = Noded(2, node4, node10)
    val node1 = Noded(1, node7, node8)
    val nodeRoot = Noded(3, node1, node2)

    BFS().bfs(nodeRoot)
}