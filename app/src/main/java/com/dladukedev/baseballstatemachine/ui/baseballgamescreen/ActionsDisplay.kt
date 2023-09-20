package com.dladukedev.baseballstatemachine.ui.baseballgamescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ActionsDisplay(validActions: ValidActionsDisplayModel, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = validActions.stateName,
            style = MaterialTheme.typography.headlineMedium
        )
        validActions.actions.chunked(3).forEach { group ->
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                group.forEach { action ->
                    Button(onClick = action.action) {
                        Text(action.displayName)
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ActionsDisplayPreview() {
    ActionsDisplay(validActions = ValidActionsDisplayModel(
        stateName = "Live Ball",
        actions = listOf(
            ActionDisplayModel("Single", {}),
            ActionDisplayModel("Double", {}),
            ActionDisplayModel("Triple", {}),
            ActionDisplayModel("Home Run", {}),
            ActionDisplayModel("Defensive Out", {})
        ),
    ))
}
