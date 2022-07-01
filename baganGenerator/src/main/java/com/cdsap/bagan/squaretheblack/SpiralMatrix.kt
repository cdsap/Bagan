package com.cdsap.bagan.squaretheblack

class SpiralMatrix {

    fun spiralOrder(matrix: Array<IntArray>): List<Int> {
       matrix.forEach {
           it.forEach {
               println(it)
           }
       }

        return emptyList()
    }

}

fun main(){
    val s = arrayOf(intArrayOf(1,2,3), intArrayOf(4,5,6), intArrayOf(7,8,9))
    SpiralMatrix().spiralOrder(s)
}


