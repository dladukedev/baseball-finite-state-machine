package com.dladukedev.baseballstatemachine.statemachine.transforms

import com.dladukedev.baseballstatemachine.statemachine.GameState

fun recordOut(gameState: GameState): GameState {
    return gameState.copy(outs = gameState.outs + 1)
}