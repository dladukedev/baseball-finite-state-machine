package com.dladukedev.baseballstatemachine.ui

fun Int.asInningCountDisplay(): String {
    val suffix = when (this % 100) {
        in 11..13 -> "th"
        else -> when (this % 10) {
            1 -> "st"
            2 -> "nd"
            3 -> "rd"
            else -> "th"
        }
    }

    return "$this$suffix"

}