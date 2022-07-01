package com.cdsap.bagan.squaretheblack

class Dijkstra {
}


data class Graph(
    val vertices: Set<Noded>,
    val edges: Map<Noded, Set<Noded>>,
    val weights: Map<Pair, Int>
)
//
//
//data class Pair(val first: Noded, val second:Noded)