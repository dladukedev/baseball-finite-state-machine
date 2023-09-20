package com.dladukedev.baseballstatemachine.statemachine.transforms

import com.dladukedev.baseballstatemachine.statemachine.models.Bases
import com.dladukedev.baseballstatemachine.statemachine.GameState

fun applyBaseChange(gameState: GameState, bases: Bases, runs: Int): GameState {
    val homeScore =
        if (gameState.inning.isBottom) gameState.homeScore + runs else gameState.homeScore
    val awayScore = if (gameState.inning.isTop) gameState.awayScore + runs else gameState.awayScore

    return gameState.copy(
        homeScore = homeScore,
        awayScore = awayScore,
        bases = bases,
    )
}