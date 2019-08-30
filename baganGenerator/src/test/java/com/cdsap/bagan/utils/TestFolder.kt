package com.cdsap.bagan.utils

import java.io.File

object TestFolder {
    fun recursiveDelete(file: File) {
        val files = file.listFiles()
        if (files != null) {
            for (each in files) {
                recursiveDelete(each)
            }
        }
        file.delete()
    }
}
