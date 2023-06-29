package com.leinardi.forlago.ext

fun String.toSnakeCase(): String {
    var result = ""
    for (i in indices) {
        val char = this[i]
        if (char.isUpperCase()) {
            result += "_${char.lowercaseChar()}"
        } else {
            result += char
        }
    }
    return if (result.startsWith("_")) result.substring(1) else result
}

fun String.isSnakeCase(): Boolean = matches(Regex("[a-z0-9]+(_[a-z0-9]+)*"))
