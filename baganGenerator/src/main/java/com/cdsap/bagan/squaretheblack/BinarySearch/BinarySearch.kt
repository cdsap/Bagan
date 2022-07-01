package com.cdsap.bagan.squaretheblack.BinarySearch

class BinarySearch {

    fun search(value: Int, start: Int, finish: Int, array: Array<Int>): Boolean {
        println("paso")
        val partitionPoint = (finish - start) / 2

        if (value == array[partitionPoint]) {
            return true
        } else if (value < array[partitionPoint]) {
            return search(value, start, partitionPoint, array)
        } else if (value > array[partitionPoint]) {
            return search(value, partitionPoint, finish, array)
        }
        return false
    }
}

fun main(){
    val binarySearch = BinarySearch()
    val arr = arrayOf(1,3,4,5,7,8,9,10)
    val a = binarySearch.search(1, 0,arr.size-1,arr)
    println(a)
}