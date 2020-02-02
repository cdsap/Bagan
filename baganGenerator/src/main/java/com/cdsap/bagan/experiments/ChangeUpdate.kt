package com.cdsap.bagan.experiments

class ChangeUpdate(
    val file: String,
    private val change: String
) {

    fun changeCode() = "sed '\$s/}/$change }/' $file > temp.kt && mv temp.kt $file && rm temp.kt"

}
