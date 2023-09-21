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

            if (result is StateMachine.Transition.Valid) {
                when (result.sideEffect) {
                    SideEffect.AnnounceHomeRun ->
                        Toast.makeText(context, "HOME RUN!!!", Toast.LENGTH_SHORT).show()
                    SideEffect.WarnGameNotOver ->
                        Toast.makeText(context, "Game Is Not Over", Toast.LENGTH_SHORT).show()
                    SideEffect.WarnGameOver ->
                        Toast.makeText(context, "Game Is Over", Toast.LENGTH_SHORT).show()
                    SideEffect.WarnInningSideNotOver ->
                        Toast.makeText(context, "Inning Side Is Not Over", Toast.LENGTH_SHORT)
                            .show()
                    SideEffect.WarnInningSideOver ->
                        Toast.makeText(context, "Inning Side Is Over", Toast.LENGTH_SHORT).show()
                    null -> { /* no-op */ }
                }
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
        is State.GameStarted -> ValidActionsDisplayModel(
            stateName = "Game Started",
            actions = listOf(
                ActionDisplayModel("Play Ball", requestTransition(Event.OnPlayBall))
            ),
        )

        is State.InningSideStarted -> ValidActionsDisplayModel(
            stateName = "Inning Side Start",
            actions = listOf(
                ActionDisplayModel(
                    "Batter Up",
                    requestTransition(Event.OnBatterUp),
                )
            ),
        )

        is State.BatterUp -> ValidActionsDisplayModel(
            stateName = "Batter Up",
            actions = listOf(
                ActionDisplayModel("Batter Out", requestTransition(Event.OnBatterOut)),
                ActionDisplayModel("Walk Batter", requestTransition(Event.OnBatterWalk)),
                ActionDisplayModel("Contact", requestTransition(Event.OnBatterContact)),
                ActionDisplayModel("Game Over", requestTransition(Event.OnGameOver)),
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

        is State.BatterOut -> ValidActionsDisplayModel(
            stateName = "Batter Out",
            actions = listOf(
                ActionDisplayModel("Side Retired", requestTransition(Event.OnSideRetired)),
                ActionDisplayModel("Batter Up", requestTransition(Event.OnBatterUp)),
            ),
        )

        is State.InningSideEnded -> ValidActionsDisplayModel(
            stateName = "Inning Side End",
            actions = listOf(
                ActionDisplayModel("Advance Inning", requestTransition(Event.OnNextInningSide)),
                ActionDisplayModel("Game Over", requestTransition(Event.OnGameOver)),
            ),
        )

        is State.GameOver -> ValidActionsDisplayModel(
            stateName = "Game Over",
            actions = listOf(
                ActionDisplayModel("New Game", requestTransition(Event.OnNewGame))
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
