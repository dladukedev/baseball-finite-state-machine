package com.dladukedev.baseballstatemachine.statemachine.guards

import com.dladukedev.baseballstatemachine.statemachine.GameState

fun isSideOut(gameState: GameState): Boolean {
    return gameState.outs >= 3
}