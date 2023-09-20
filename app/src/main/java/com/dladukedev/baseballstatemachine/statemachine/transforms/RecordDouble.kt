package com.dladukedev.baseballstatemachine.statemachine.transforms

import com.dladukedev.baseballstatemachine.statemachine.models.Bases
import com.dladukedev.baseballstatemachine.statemachine.GameState

fun recordDouble(gameState: GameState): GameState {
    val runs = listOf(gameState.bases.onSecond, gameState.bases.onThird).count { it }

    val bases = Bases(
        onFirst = false,
        onSecond = true,
        onThird = gameState.bases.onFirst,
    )

    return applyBaseChange(gameState, bases, runs)
}