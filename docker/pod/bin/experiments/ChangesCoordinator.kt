@file:Include("Change.kt")
@file:Include("ChangeUpdate.kt")


package com.cdsap.bagan.experiments

class ChangesCoordinator {
    var internalCounter = 0

    fun change(type: Change): String {
        return when (type) {
            Change.ADD_VALUE -> changeValue()
            Change.ADD_METHOD -> changeMethod()
        }
    }

    private fun changeValue(): String = "val newValue${internalCounter++} = $internalCounter"

    private fun changeMethod(): String = "  fun myNewFunction${internalCounter++}() {println($internalCounter) }"
}
