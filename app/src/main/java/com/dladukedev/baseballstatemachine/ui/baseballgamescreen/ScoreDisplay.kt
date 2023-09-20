package com.dladukedev.baseballstatemachine.ui.baseballgamescreen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ScoreDisplay(
    scoreboard: ScoreboardDisplayModel,
    modifier: Modifier = Modifier,
) {

    Row(modifier = modifier) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(scoreboard.awayTeamName, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = scoreboard.awayTeamScore,
                style = MaterialTheme.typography.displayLarge
            )
        }

        Text(
            text = " - ",
            style = MaterialTheme.typography.displayLarge,
            modifier = Modifier.align(Alignment.Bottom)
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(scoreboard.homeTeamName, style = MaterialTheme.typography.headlineMedium)
            Text(
                text = scoreboard.homeTeamScore,
                style = MaterialTheme.typography.displayLarge
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ScoreDisplayPreview() {
    ScoreDisplay(
        scoreboard = ScoreboardDisplayModel(
            homeTeamName = "Home",
            awayTeamName = "Away",
            homeTeamScore = "10",
            awayTeamScore = "5",
        ),
    )
}
