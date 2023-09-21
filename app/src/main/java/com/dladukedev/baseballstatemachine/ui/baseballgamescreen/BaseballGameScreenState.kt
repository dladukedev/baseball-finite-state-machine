package com.dladukedev.baseballstatemachine.ui.baseballgamescreen

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.ui.platform.LocalContext
import com.dladukedev.baseballstatemachine.statemachine.models.Bases
import com.dladukedev.baseballstatemachine.statemachine.Event
import com.dladukedev.baseballstatemachine.statemachine.models.HitType
import com.dladukedev.baseballstatemachine.statemachine.models.InningSide
import com.dladukedev.baseballstatemachine.statemachine.SideEffect
import com.dladukedev.baseballstatemachine.statemachine.State
import com.dladukedev.baseballstatemachine.statemachine.baseballStateMachine
import com.dladukedev.baseballstatemachine.ui.asInningCountDisplay
import com.dladukedev.baseballstatemachine.ui.rememberStateMachine
import com.tinder.StateMachine

data class ScoreboardDisplayModel(
    val awayTeamName: String,
    val awayTeamScore: String,
    val homeTeamName: String,
    val homeTeamScore: String,
)

data class InningDisplayModel(
    val ordinal: String,
    val side: InningSide,
)

data class ActionDisplayModel(
    val displayName: String,
    val action: () -> Unit,
)

@Immutable
data class ValidActionsDisplayModel(
    val stateName: String,
    val actions: List<ActionDisplayModel>,
)

data class BaseballGameScreenState(
    val requestTransition: (Event) -> () -> Unit,

    val scoreboard: ScoreboardDisplayModel,
    val inning: InningDisplayModel,
    val bases: Bases,
    val outs: String,

    val validActions: ValidActionsDisplayModel,
)

@Composable
fun rememberBaseballGameState(
    context: Context = LocalContext.current,
): BaseballGameScreenState {
    val (state, transition) = rememberStateMachine(stateMachine = baseballStateMachine)

    val requestTransition: (Event) -> () -> Unit = { event ->
        {
            val result = transition(event)

            if (result is StateMachine.Transition.Valid && result.sideEffect is SideEffect.AnnounceHomeRun) {
                Toast.makeText(context, "HOME RUN!!!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    val scoreboard = ScoreboardDisplayModel(
        awayTeamName = "Away",
        homeTeamName = "Home",
        awayTeamScore = state.gameState.awayScore.toString(),
        homeTeamScore = state.gameState.homeScore.toString(),
    )

    val inning = InningDisplayModel(
        ordinal = state.gameState.inning.number.asInningCountDisplay(),
        side = state.gameState.inning.side,
    )

    val bases = state.gameState.bases

    val outs = "${state.gameState.outs} Out(s)"

    val validTransitions = when (state) {
        is State.BatterUp -> ValidActionsDisplayModel(
            stateName = "Batter Up",
            actions = listOf(
                ActionDisplayModel("Batter Out", requestTransition(Event.OnBatterOut)),
                ActionDisplayModel("Walk Batter", requestTransition(Event.OnBatterWalk)),
                ActionDisplayModel("Live Ball", requestTransition(Event.OnBatterContact)),
            ),
        )

        is State.GameOver -> ValidActionsDisplayModel(
            stateName = "Game Over",
            actions = listOf(
                ActionDisplayModel("New Game", requestTransition(Event.OnNewGame))
            ),
        )

        is State.GameStarted -> ValidActionsDisplayModel(
            stateName = "Game Started",
            actions = listOf(
                ActionDisplayModel("Play Ball", requestTransition(Event.OnPlayBall))
            ),
        )

        is State.LiveBall -> ValidActionsDisplayModel(
            stateName = "Live Ball",
            actions = listOf(
                ActionDisplayModel("Single", requestTransition(Event.OnHit(HitType.SINGLE))),
                ActionDisplayModel("Double", requestTransition(Event.OnHit(HitType.DOUBLE))),
                ActionDisplayModel("Triple", requestTransition(Event.OnHit(HitType.TRIPLE))),
                ActionDisplayModel(
                    "Home Run",
                    requestTransition(Event.OnHit(HitType.HOME_RUN))
                ),
                ActionDisplayModel("Defensive Out", requestTransition(Event.OnDefensiveOut))
            ),
        )
    }

    return BaseballGameScreenState(
        requestTransition = requestTransition,
        scoreboard = scoreboard,
        inning = inning,
        bases = bases,
        outs = outs,
        validActions = validTransitions,
    )
}
