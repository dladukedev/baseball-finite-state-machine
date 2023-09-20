package com.dladukedev.baseballstatemachine.statemachine.transforms

import com.dladukedev.baseballstatemachine.statemachine.models.Bases
import com.dladukedev.baseballstatemachine.statemachine.GameState

fun recordSingle(gameState: GameState): GameState {
    val runs = when {
        gameState.bases.onThird -> 1
        else -> 0
    }

    val bases = Bases(
        onFirst = true,
        onSecond = gameState.bases.onFirst,
        onThird = gameState.bases.onSecond
    )

    return applyBaseChange(gameState, bases, runs)
}