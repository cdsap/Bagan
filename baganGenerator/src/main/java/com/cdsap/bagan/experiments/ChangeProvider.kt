package com.cdsap.bagan.experiments

class ChangeProvider {
    var internalCounter = 0

    fun change(type: TypeChange): String {
        return when (type) {
            TypeChange.ADD_VALUE -> addValue()
            TypeChange.ADD_METHOD -> addMethod()
        }
    }

    private fun addValue(): String = "val newValue${internalCounter++} = $internalCounter"
    private fun addMethod(): String = "  fun myNewFunction${internalCounter++}() {println($internalCounter) }"
}
