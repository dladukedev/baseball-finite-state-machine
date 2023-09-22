package com.dladukedev.baseballstatemachine

import com.dladukedev.baseballstatemachine.statemachine.Event
import com.dladukedev.baseballstatemachine.statemachine.GameState
import com.dladukedev.baseballstatemachine.statemachine.SideEffect
import com.dladukedev.baseballstatemachine.statemachine.State
import com.dladukedev.baseballstatemachine.statemachine.baseballStateMachine
import com.dladukedev.baseballstatemachine.statemachine.models.Bases
import com.dladukedev.baseballstatemachine.statemachine.models.HitType
import com.tinder.StateMachine
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleStateMachineTest {
    @Test
    fun `inning with home run has correct flow`() {
        assertTrue(baseballStateMachine.state is State.GameStarted)

        // Start Game
        baseballStateMachine.transition(Event.OnPlayBall)
        assertTrue(baseballStateMachine.state is State.BatterUp)

        // Home Run
        baseballStateMachine.transition(Event.OnBatterContact)
        assertTrue(baseballStateMachine.state is State.LiveBall)
        val result = baseballStateMachine.transition(Event.OnHit(HitType.HOME_RUN))
        assertTrue(baseballStateMachine.state is State.BatterUp)
        assertEquals(baseballStateMachine.state.gameState, GameState(awayScore = 1))
        assertTrue(result is StateMachine.Transition.Valid)
        assertEquals((result as StateMachine.Transition.Valid).sideEffect, SideEffect.AnnounceHomeRun)
    }
}