package com.cdsap.kscript.entities
/**
 * Element represents an entity on source to be included in the replacer action.
 * To run kscript we need to update the dependencies on the header.
 * Every Element includes the type and action used to define the category of replacement.
 */
data class Element(
    val nameFile: String,
    val dependencies: String,
    val type: Type,
    val action: Action,
    val dir: String
)