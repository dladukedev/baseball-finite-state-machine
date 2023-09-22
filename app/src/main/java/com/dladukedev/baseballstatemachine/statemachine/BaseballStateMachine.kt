package com.dladukedev.baseballstatemachine.statemachine

import com.dladukedev.baseballstatemachine.statemachine.guards.isGameOver
import com.dladukedev.baseballstatemachine.statemachine.guards.isSideOut
import com.dladukedev.baseballstatemachine.statemachine.models.Bases
import com.dladukedev.baseballstatemachine.statemachine.models.HitType
import com.dladukedev.baseballstatemachine.statemachine.models.Inning
import com.dladukedev.baseballstatemachine.statemachine.models.InningSide
import com.dladukedev.baseballstatemachine.statemachine.transforms.advanceInningSide
import com.dladukedev.baseballstatemachine.statemachine.transforms.recordHit
import com.dladukedev.baseballstatemachine.statemachine.transforms.recordOut
import com.dladukedev.baseballstatemachine.statemachine.transforms.recordWalk
import com.tinder.StateMachine

data class GameState(
    val inning: Inning = Inning(1, InningSide.TOP),
    val homeScore: Int = 0,
    val awayScore: Int = 0,
    val outs: Int = 0,
    val bases: Bases = Bases(onFirst = false, onSecond = false, onThird = false),
)

sealed class State(val gameState: GameState) {
    data class GameStarted(private val initialState: GameState) : State(initialState)

    data class BatterUp(private val updatedState: GameState) : State(updatedState)

    data class LiveBall(private val updatedState: GameState) : State(updatedState)

    data class GameOver(private val updatedState: GameState) : State(updatedState)
}

sealed class Event {
    object OnPlayBall : Event()

    object OnBatterOut : Event()
    object OnBatterWalk : Event()
    object OnBatterContact : Event()

    data class OnHit(val hitType: HitType) : Event()
    object OnDefensiveOut : Event()

    object OnNewGame : Event()
}

sealed class SideEffect {
    object AnnounceHomeRun : SideEffect()
}

val baseballStateMachine = StateMachine.create<State, Event, SideEffect> {
    initialState(State.GameStarted(GameState()))

    state<State.GameStarted> {
        on<Event.OnPlayBall> {
            transitionTo(State.BatterUp(gameState))
        }
    }

    state<State.BatterUp> {
        on<Event.OnBatterOut> {
            val newGameState = recordOut(gameState)

            if (isGameOver(newGameState)) {
                transitionTo(State.GameOver(newGameState))
            } else if (isSideOut(newGameState)) {
                val switchSidesGameState = advanceInningSide(gameState)
                transitionTo(State.BatterUp(switchSidesGameState))
            } else {
                transitionTo(State.BatterUp(newGameState))
            }
        }

        on<Event.OnBatterContact> {
            transitionTo(State.LiveBall(gameState))
        }

        on<Event.OnBatterWalk> {
            val newState = recordWalk(gameState)

            if (isGameOver(newState)) {
                transitionTo(State.GameOver(newState))
            } else {
                transitionTo(State.BatterUp(newState))
            }
        }
    }

    state<State.LiveBall> {
        on<Event.OnHit> {
            val newState = recordHit(gameState, it.hitType)

            val sideEffect =
                if (it.hitType == HitType.HOME_RUN) SideEffect.AnnounceHomeRun else null

            if (isGameOver(newState)) {
                transitionTo(State.GameOver(newState), sideEffect)
            } else {
                transitionTo(State.BatterUp(newState), sideEffect)
            }
        }
        on<Event.OnDefensiveOut> {
            val newGameState = recordOut(gameState)

            if (isGameOver(newGameState)) {
                transitionTo(State.GameOver(newGameState))
            } else if (isSideOut(newGameState)) {
                val switchSidesGameState = advanceInningSide(gameState)
                transitionTo(State.BatterUp(switchSidesGameState))
            } else {
                transitionTo(State.BatterUp(newGameState))
            }
        }
    }

    state<State.GameOver> {
        on<Event.OnNewGame> {
            transitionTo(State.GameStarted(GameState()))
        }
    }
}