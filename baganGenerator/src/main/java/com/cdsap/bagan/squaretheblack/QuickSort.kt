package com.cdsap.bagan.squaretheblack

class QuickSort {
    fun quicksort(items: List<Int>): List<Int> {
        if (items.count() < 2) {
            return items
        }
        val pivot = (items.count()) / 2
        val less = items.filter { it < items[pivot] }
        val equal = items.filter { it == items[pivot] }
        val greater = items.filter { it > items[pivot] }

        return quicksort(less) + equal + quicksort(greater)

    }

}


fun main() {
    val list = listOf(3, 5, 1, 6, 32, 10, 4, 7)

    val a = QuickSort().quicksort(list)

    //  println(a)

    val no3 = Not(3, null)
    val no2 = Not(2, no3)
    val no1 = Not(1, no2)
    val no0 = Not(0, no1)

//    println("iterating")
//    var aa: Not? = no0
//    while (aa != null) {
//        println(aa.value)
//        aa = aa.next
//    }
//
//    var bb: Not? = no0
//    var next: Not? = null
//    var previous: Not? = null
//    var current = bb
//    while (current != null) {
//        next = current?.next
//        current?.next = previous
//        previous = current
//        current = next
//    }
//    println("reversing iterarive")
//
//    var x = previous
//    while (x != null) {
//        println(x.value)
//        x = x?.next
//    }
//

    var vv: Not? = no0
    var vva: Not? = no0


    println("reversing recursive")
    println("reversing recursivewwwwwwwwww")
    while (vv != null) {
        println(vv?.value)
        vv = vv?.next
    }

    println("reversing recursivewwwwww22332wwww")
    val xc = reverstLing(vva)
    println(xc)
    var x1 = xc
    while (x1 != null) {
        println(x1?.value)
        x1 = x1?.next
    }

}

fun reverstLing(head: Not?): Not? {
    if (head?.next == null) {
        return head
    }
    val newNode = reverstLing(head.next)

    head.next!!.next = head
    head.next = null
    return newNode
}

data class Not(val value: Int, var next: Not?)