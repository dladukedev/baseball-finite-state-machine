package com.dladukedev.baseballstatemachine.statemachine.transforms

import com.dladukedev.baseballstatemachine.statemachine.models.Bases
import com.dladukedev.baseballstatemachine.statemachine.GameState

fun recordHomeRun(gameState: GameState): GameState {
    val runs = gameState.bases.runnerCount + 1

    val bases = Bases(onFirst = false, onSecond = false, onThird = false)

    return applyBaseChange(gameState, bases, runs)
}