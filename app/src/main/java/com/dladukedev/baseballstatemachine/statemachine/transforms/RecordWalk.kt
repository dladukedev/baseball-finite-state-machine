package com.dladukedev.baseballstatemachine.statemachine.transforms

import com.dladukedev.baseballstatemachine.statemachine.models.Bases
import com.dladukedev.baseballstatemachine.statemachine.GameState

fun recordWalk(gameState: GameState): GameState {
    val runs = when {
        gameState.bases.areLoaded -> 1
        else -> 0
    }

    val bases = when {
        gameState.bases.areLoaded -> gameState.bases
        gameState.bases.runnerCount == 2 -> Bases(onFirst = true, onSecond = true, onThird = true)
        gameState.bases.onFirst || gameState.bases.onSecond -> Bases(
            onFirst = true,
            onSecond = true,
            onThird = false
        )

        gameState.bases.onThird -> Bases(onFirst = true, onSecond = false, onThird = true)
        else -> Bases(onFirst = true, onSecond = false, onThird = false)
    }

    return applyBaseChange(gameState, bases, runs).copy(bases = bases)
}

