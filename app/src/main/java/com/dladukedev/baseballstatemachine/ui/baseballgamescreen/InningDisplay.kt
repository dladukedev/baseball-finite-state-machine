package com.dladukedev.baseballstatemachine.ui.baseballgamescreen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.dladukedev.baseballstatemachine.statemachine.models.InningSide

@Composable
fun InningDisplay(inning: InningDisplayModel, modifier: Modifier = Modifier) {
    Row(horizontalArrangement = Arrangement.End, modifier = modifier) {
        when (inning.side) {
            InningSide.TOP -> Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = null,
                modifier = Modifier.rotate(180f)
            )

            InningSide.BOTTOM -> Icon(
                imageVector = Icons.Default.ArrowDropDown, contentDescription = null
            )
        }

        Text(
            text = inning.ordinal,
            style = MaterialTheme.typography.labelLarge,
            modifier = Modifier.align(Alignment.CenterVertically)
        )

    }
}

class InningDisplayPreviewParameter : PreviewParameterProvider<InningDisplayModel> {
    override val values = sequenceOf(
        InningDisplayModel("1st", InningSide.TOP),
        InningDisplayModel("5th", InningSide.BOTTOM),
        InningDisplayModel("12th", InningSide.TOP)
    )
}

@Preview(showBackground = true)
@Composable
private fun InningDisplayPreview(
    @PreviewParameter(InningDisplayPreviewParameter::class) inning: InningDisplayModel
) {
    MaterialTheme {
        InningDisplay(inning = inning)
    }
}