package com.cdsap.bagan.squaretheblack

class InverseBinaryTree {

    fun inverse(noded: Noded?) {
        if (noded != null) {
            val oldLeft = noded.nodeLeft
            val oldRight = noded.nodeRight
            noded.nodeLeft = noded.nodeRight
            noded.nodeRight = oldLeft
            inverse(noded.nodeLeft)
            inverse(noded.nodeRight)
        }

    }
}

data class Noded(val value: Int, var nodeLeft: Noded?, var nodeRight: Noded?)


fun main() {
    println("d")
    val node10 = Noded(10, null, null)
    val node4 = Noded(4, null, null)
    val node8 = Noded(8, null, null)
    val node7 = Noded(7, null, null)
    val node2 = Noded(2, node4, node10)
    val node1 = Noded(1, node7, node8)
    val nodeRoot = Noded(3, node1, node2)

    traverse(nodeRoot)
    InverseBinaryTree().inverse(nodeRoot)
    println("traversssing ")
    traverse(nodeRoot)
}

fun traverse(node: Noded?) {
    if (node != null) {
        println(node.value)
        traverse(node.nodeLeft)
        traverse(node.nodeRight)
    }
}


/*
     3
  1       2
7   8   4   10

 */