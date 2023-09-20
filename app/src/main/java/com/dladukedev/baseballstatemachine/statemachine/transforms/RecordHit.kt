package com.dladukedev.baseballstatemachine.statemachine.transforms

import com.dladukedev.baseballstatemachine.statemachine.GameState
import com.dladukedev.baseballstatemachine.statemachine.models.HitType

fun recordHit(gameState: GameState, hit: HitType): GameState {
    return when (hit) {
        HitType.SINGLE -> recordSingle(gameState)
        HitType.DOUBLE -> recordDouble(gameState)
        HitType.TRIPLE -> recordTriple(gameState)
        HitType.HOME_RUN -> recordHomeRun(gameState)
    }
}