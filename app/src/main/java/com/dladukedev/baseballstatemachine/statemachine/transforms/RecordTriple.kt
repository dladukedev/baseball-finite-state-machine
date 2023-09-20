package com.dladukedev.baseballstatemachine.statemachine.transforms

import com.dladukedev.baseballstatemachine.statemachine.models.Bases
import com.dladukedev.baseballstatemachine.statemachine.GameState

fun recordTriple(gameState: GameState): GameState {
    val runs = gameState.bases.runnerCount

    val bases = Bases(onFirst = false, onSecond = false, onThird = true)

    return applyBaseChange(gameState, bases, runs)
}