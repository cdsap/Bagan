package com.cdsap.kscript.entities

data class Element(
    val nameFile: String,
    val dependencies: String,
    val type: Type,
    val action: Action,
    val dir: String
)