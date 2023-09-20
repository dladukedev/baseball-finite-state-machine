package com.dladukedev.baseballstatemachine.statemachine.models

data class Bases(
    val onFirst: Boolean = false,
    val onSecond: Boolean = false,
    val onThird: Boolean = false,
) {
    val runnerCount: Int = listOf(onFirst, onSecond, onThird).count { it }
    val areLoaded: Boolean = onFirst && onSecond && onThird
}