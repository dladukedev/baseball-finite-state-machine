package com.dladukedev.baseballstatemachine.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.tinder.StateMachine

data class StateMachineResult<TState : Any, TEvent : Any, TSideEffect : Any>(
    val state: TState,
    val transition: (TEvent) -> StateMachine.Transition<TState, TEvent, TSideEffect>
)

@Composable
fun <TState : Any, TEvent : Any, TSideEffect : Any> rememberStateMachine(stateMachine: StateMachine<TState, TEvent, TSideEffect>): StateMachineResult<TState, TEvent, TSideEffect> {
    var state by remember {
        mutableStateOf(stateMachine.state)
    }

    val transition: (TEvent) -> StateMachine.Transition<TState, TEvent, TSideEffect> = { event ->
        val result = stateMachine.transition(event)

        if (result is StateMachine.Transition.Valid) {
            state = result.toState
        }

        result
    }

    return StateMachineResult(
        state,
        transition,
    )
}
