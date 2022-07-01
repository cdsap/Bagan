package com.cdsap.bagan.squaretheblack

class DFS {

    fun dfs(node: Noded?) {

        if(node != null){
            println(node.value)
            dfs(node.nodeLeft)
            dfs(node.nodeRight)
        }

    }
}


fun main() {
    println("aloooo")
    val node10 = Noded(10, null, null)
    val node4 = Noded(4, null, null)
    val node8 = Noded(8, null, null)
    val node7 = Noded(7, null, null)
    val node2 = Noded(2, node4, node10)
    val node1 = Noded(1, node7, node8)
    val nodeRoot = Noded(3, node1, node2)

    DFS().dfs(nodeRoot)
}