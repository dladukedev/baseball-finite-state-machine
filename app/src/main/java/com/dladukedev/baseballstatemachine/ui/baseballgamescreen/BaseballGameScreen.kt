package com.dladukedev.baseballstatemachine.ui.baseballgamescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dladukedev.baseballstatemachine.statemachine.models.Bases
import com.dladukedev.baseballstatemachine.statemachine.models.InningSide


@Composable
fun BaseballGameScreen() {
    val screenState = rememberBaseballGameState()

    BaseballGameScreen(screenState)
}

@Composable
fun BaseballGameScreen(screenState: BaseballGameScreenState) {
    Column {
        Spacer(modifier = Modifier.height(48.dp))

        ScoreDisplay(
            scoreboard = screenState.scoreboard,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(modifier = Modifier.fillMaxWidth()) {
            InningDisplay(inning = screenState.inning, modifier = Modifier.weight(1f))

            BasesDisplay(
                bases = screenState.bases, modifier = Modifier.size(100.dp)
            )

            Text(
                text = screenState.outs,
                style = MaterialTheme.typography.labelLarge,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        ActionsDisplay(
            validActions = screenState.validActions,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(48.dp))
    }
}

@Preview(showBackground = true)
@Composable
private fun BaseballGameScreenPreview() {
    BaseballGameScreen(
        screenState =
        BaseballGameScreenState(
            requestTransition = {{}},
            scoreboard = ScoreboardDisplayModel(
                homeTeamName = "Home",
                awayTeamName = "Away",
                homeTeamScore = "10",
                awayTeamScore = "5",
            ),
            inning = InningDisplayModel(
                ordinal = "5th",
                side = InningSide.BOTTOM,
            ),
            bases = Bases(onSecond = true),
            outs = "1 Out(s)",
            validActions = ValidActionsDisplayModel(
                stateName = "Live Ball",
                actions = listOf(
                    ActionDisplayModel("Single", {}),
                    ActionDisplayModel("Double", {}),
                    ActionDisplayModel("Triple", {}),
                    ActionDisplayModel("Home Run", {}),
                    ActionDisplayModel("Defensive Out", {})
                ),
            )

        )

    )
}
