package com.dladukedev.baseballstatemachine.statemachine.guards

import com.dladukedev.baseballstatemachine.statemachine.GameState

fun isWalkoffWin(gameState: GameState): Boolean {
    val isHomeTeamWinning = gameState.homeScore > gameState.awayScore
    val isExtraInnings = gameState.inning.number > 9

    return isHomeTeamWinning && isExtraInnings
}