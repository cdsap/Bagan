package com.cdsap.bagan.squaretheblack

class FloodFill {

    fun flodFill(pannel: Array<Array<Int>>, x: Int, y: Int, newColor: Int) {
        val oldColor = pannel[x][y]
        if (oldColor != newColor) {
            //pannel[x][y] = newColor
            fill(pannel, x, y, newColor, pannel.size, pannel[0].size,oldColor)

        }
    }

    fun fill(pannel: Array<Array<Int>>, x: Int, y: Int, newColor: Int, sizeRows: Int, sizeCols: Int,oldColor: Int) {
        if (x >= sizeRows || x < 0) {

        } else if (y >= sizeCols || y < 0) {

        } else {
            val oldColora = pannel[x][y]
            if (oldColora == oldColor) {
                pannel[x][y] = newColor
                fill(pannel, x + 1, y, newColor, sizeRows, sizeCols,oldColor)
                fill(pannel, x - 1, y, newColor, sizeRows, sizeCols,oldColor)
                fill(pannel, x, y + 1, newColor, sizeRows, sizeCols,oldColor)
                fill(pannel, x, y - 1, newColor, sizeRows, sizeCols,oldColor)
            }
        }
    }
}

fun main() {
    val flodFill = FloodFill()
    val pannel = arrayOf(
        arrayOf(1, 0, 2, 1),
        arrayOf(1, 0, 0, 1),
        arrayOf(1, 0, 2, 2)
    )

    pannel.forEach {
        it.forEach {
            print(it)
        }
        println()
    }
    flodFill.flodFill(pannel, 1, 1, 3)

    println("after")
    pannel.forEach {
        it.forEach {
            print(it)
        }
        println()
    }
}