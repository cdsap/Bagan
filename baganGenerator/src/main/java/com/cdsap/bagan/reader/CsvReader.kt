package com.cdsap.bagan.reader

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

class CsvReader {

    fun read(){
        csvReader().open("test.csv") {
            var count =0
            readAllAsSequence().forEach { row ->
                //Do something
          //      println(row) //[a, b, c]
                
                
                println("""
                      {
                                    "module": ":${row.get(0)}",
                                    "path": "${row.get(1)}/${row.get(2)}"
                      },
                """.trimIndent())
//                
//                println("""
//                    sed \inaki/}/  fun myNewFunction$count() {println(1) } }/' ${row.get(1)}/${row.get(2)} > temp.kt \
//                     && mv temp.kt ${row.get(1)}/${row.get(2)}  && rm temp.kt
//                """.trimIndent())
                count++
            }
        }
    }
}

fun main() {
    val c = CsvReader().read()
}