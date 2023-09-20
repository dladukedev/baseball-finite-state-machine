package com.dladukedev.baseballstatemachine.statemachine.transforms

import com.dladukedev.baseballstatemachine.statemachine.models.Bases
import com.dladukedev.baseballstatemachine.statemachine.GameState
import com.dladukedev.baseballstatemachine.statemachine.models.Inning
import com.dladukedev.baseballstatemachine.statemachine.models.InningSide

fun advanceInningSide(gameState: GameState): GameState {
    val currentInning = gameState.inning

    val nextInning = when (currentInning.side) {
        InningSide.TOP -> currentInning.copy(side = InningSide.BOTTOM)
        InningSide.BOTTOM -> Inning(number = currentInning.number + 1, side = InningSide.TOP)
    }

    return gameState.copy(inning = nextInning, outs = 0, bases = Bases())
}