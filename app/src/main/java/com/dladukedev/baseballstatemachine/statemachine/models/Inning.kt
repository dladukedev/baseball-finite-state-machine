package com.dladukedev.baseballstatemachine.statemachine.models

data class Inning(
    val number: Int,
    val side: InningSide,
) {
    val isBottom: Boolean = side == InningSide.BOTTOM
    val isTop: Boolean = side == InningSide.TOP
}