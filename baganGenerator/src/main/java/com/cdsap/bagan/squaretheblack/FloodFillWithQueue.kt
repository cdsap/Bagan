package com.cdsap.bagan.squaretheblack

import java.util.*

class Pair(val first: Int, val second: Int)

class FloodFillWithQueue {

    var queue: Queue<Pair> = LinkedList<Pair>()
    fun flodFill(pannel: Array<Array<Int>>, x: Int, y: Int, newColor: Int) {
        val oldColor = pannel[x][y]
        if (oldColor != newColor) {
            //pannel[x][y] = newColor
            queue.add(Pair(x, y))
            while (!queue.isEmpty()) {
                val element = queue.poll()
                if ((element.first >= 0 && element.first < pannel.size) && (element.second >= 0 && element.second < pannel[0].size)) {
                    if (pannel[element.first][element.second] == oldColor) {
                        pannel[element.first][element.second] = newColor
                        queue.add(Pair(element.first + 1, element.second))
                        queue.add(Pair(element.first - 1, element.second))
                        queue.add(Pair(element.first, element.second + 1))
                        queue.add(Pair(element.first, element.second - 1))

                    }
                }
            }
        }
    }

    fun fill(pannel: Array<Array<Int>>, x: Int, y: Int, newColor: Int, sizeRows: Int, sizeCols: Int, oldColor: Int) {

        if (x >= sizeRows || x < 0) {

        } else if (y >= sizeCols || y < 0) {

        } else {
            val oldColora = pannel[x][y]
            if (oldColora == oldColor) {
                pannel[x][y] = newColor
                fill(pannel, x + 1, y, newColor, sizeRows, sizeCols, oldColor)
                fill(pannel, x - 1, y, newColor, sizeRows, sizeCols, oldColor)
                fill(pannel, x, y + 1, newColor, sizeRows, sizeCols, oldColor)
                fill(pannel, x, y - 1, newColor, sizeRows, sizeCols, oldColor)
            }
        }
    }
}

fun main() {
    val flodFill = FloodFillWithQueue()
    val pannel = arrayOf(
        arrayOf(1, 0, 2, 1),
        arrayOf(1, 0, 0, 1),
        arrayOf(1, 0, 2, 2),
        arrayOf(1, 0, 2, 0)
    )

    pannel.forEach {
        it.forEach {
            print(it)
        }
        println()
    }
    flodFill.flodFill(pannel, 1, 1, 3)

    println("aftewwwwr")
    pannel.forEach {
        it.forEach {
            print(it)
        }
        println()
    }
}