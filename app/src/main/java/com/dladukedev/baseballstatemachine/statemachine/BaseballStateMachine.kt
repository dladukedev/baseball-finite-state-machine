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

    data class InningSideStarted(private val updatedState: GameState) : State(updatedState)

    data class BatterUp(private val updatedState: GameState) : State(updatedState)

    data class LiveBall(private val updatedState: GameState) : State(updatedState)

    data class BatterOut(private val updatedState: GameState) : State(updatedState)

    data class InningSideEnded(private val updatedState: GameState) : State(updatedState)

    data class GameOver(private val updatedState: GameState) : State(updatedState)
}

sealed class Event {
    object OnPlayBall : Event()

    object OnBatterUp : Event()

    object OnBatterOut : Event()
    object OnBatterWalk : Event()
    object OnBatterContact : Event()

    data class OnHit(val hitType: HitType) : Event()
    object OnDefensiveOut : Event()

    object OnSideRetired : Event()

    object OnNextInningSide : Event()

    object OnGameOver : Event()

    object OnNewGame : Event()
}

sealed class SideEffect {
    object AnnounceHomeRun : SideEffect()

    object WarnGameOver: SideEffect()
    object WarnGameNotOver: SideEffect()
    object WarnInningSideOver: SideEffect()
    object WarnInningSideNotOver: SideEffect()
}

val baseballStateMachine = StateMachine.create<State, Event, SideEffect> {
    initialState(State.GameStarted(GameState()))

    state<State.GameStarted> {
        on<Event.OnPlayBall> {
            transitionTo(State.InningSideStarted(gameState))
        }
    }

    state<State.InningSideStarted> {
        on<Event.OnBatterUp> {
            transitionTo(State.BatterUp(gameState))
        }
    }

    state<State.BatterUp> {
        on<Event.OnBatterOut> {
            if (isGameOver(gameState)) {
                return@on dontTransition(SideEffect.WarnGameOver)
            }

            val newGameState = recordOut(gameState)

            transitionTo(State.BatterOut(newGameState))
        }

        on<Event.OnBatterContact> {
            if (isGameOver(gameState)) {
                return@on dontTransition(SideEffect.WarnGameOver)
            }

            transitionTo(State.LiveBall(gameState))
        }

        on<Event.OnBatterWalk> {
            if (isGameOver(gameState)) {
                return@on dontTransition(SideEffect.WarnGameOver)
            }

            val newState = recordWalk(gameState)

            transitionTo(State.BatterUp(newState))
        }

        on<Event.OnGameOver> {
            if (isGameOver(gameState).not()) {
                return@on dontTransition(SideEffect.WarnGameNotOver)
            }

            transitionTo(State.InningSideEnded(gameState))
        }
    }

    state<State.LiveBall> {
        on<Event.OnHit> {
            val newState = recordHit(gameState, it.hitType)

            val sideEffect =
                if (it.hitType == HitType.HOME_RUN) SideEffect.AnnounceHomeRun else null

            transitionTo(State.BatterUp(newState), sideEffect)
        }
        on<Event.OnDefensiveOut> {
            val newGameState = recordOut(gameState)

            transitionTo(State.BatterOut(newGameState))
        }
    }

    state<State.BatterOut> {
        on<Event.OnSideRetired> {
            if (isSideOut(gameState).not()) {
                return@on dontTransition(SideEffect.WarnInningSideNotOver)
            }

            transitionTo(State.InningSideEnded(gameState))
        }

        on<Event.OnBatterUp> {
            if (isSideOut(gameState)) {
                return@on dontTransition(SideEffect.WarnInningSideOver)
            }

            transitionTo(State.BatterUp(gameState))
        }
    }

    state<State.InningSideEnded> {
        on<Event.OnNextInningSide> {
            if (isGameOver(gameState)) {
                return@on dontTransition(SideEffect.WarnInningSideOver)
            }

            val newGameState = advanceInningSide(gameState)
            transitionTo(State.InningSideStarted(newGameState))
        }

        on<Event.OnGameOver> {
            if (isGameOver(gameState).not()) {
                return@on dontTransition(SideEffect.WarnGameNotOver)
            }

            transitionTo(State.GameOver(gameState))
        }
    }

    state<State.GameOver> {
        on<Event.OnNewGame> {
            transitionTo(State.GameStarted(GameState()))
        }
    }
}