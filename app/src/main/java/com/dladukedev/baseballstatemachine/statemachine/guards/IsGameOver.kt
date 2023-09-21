package com.dladukedev.baseballstatemachine.statemachine.guards

import com.dladukedev.baseballstatemachine.statemachine.GameState
import com.dladukedev.baseballstatemachine.statemachine.models.InningSide

fun isGameOver(gameState: GameState): Boolean {
    val isNotTied = gameState.awayScore != gameState.homeScore
    val isLastInning = gameState.inning.number >= 9 && gameState.inning.side == InningSide.BOTTOM
    val isSideOut = isSideOut(gameState)

    val isGameOverInRegulation = isNotTied && isLastInning && isSideOut

    val isHomeTeamWinning = gameState.homeScore > gameState.awayScore
    val isTopOfLastInning = gameState.inning.number == 9 && gameState.inning.isTop

    val isHomeTeamWinSkipBottomOfInning = isHomeTeamWinning && isTopOfLastInning && isSideOut

    val isWalkOffWin = isLastInning && isHomeTeamWinning

    return isGameOverInRegulation || isHomeTeamWinSkipBottomOfInning || isWalkOffWin
}