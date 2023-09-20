package com.dladukedev.baseballstatemachine.ui.baseballgamescreen

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dladukedev.baseballstatemachine.statemachine.models.Bases
import kotlin.math.sqrt

@Composable
fun BasesDisplay(bases: Bases, modifier: Modifier = Modifier) {
    val emptyBaseColor = MaterialTheme.colorScheme.onBackground
    val occupiedBaseColor = MaterialTheme.colorScheme.primary

    Canvas(modifier = modifier) {
        val smallDimen = minOf(size.width, (size.height * 1.35f))
        val maxSize = Size(smallDimen, smallDimen)
        val gap = maxOf(smallDimen / 25f, 1f)
        val strokeWidth = maxOf(smallDimen / 50f, 1f)

        val height = 1.5f * (smallDimen / 3f) * sqrt(2f)
        val y = (size.height - height) / 2

        val offsetX = if (size.width > size.height) {
            size.width / 6f
        } else 0f
        val offsetY = if (size.height > (size.width / 1.35f)) {
            y
        } else 0f

        translate(left = (smallDimen / 6f) + offsetX, top = (smallDimen / 7.25f) + offsetY) {
            rotate(degrees = 45f, pivot = Offset(x = smallDimen / 3f, y = smallDimen / 3f)) {

                drawBase(
                    topLeft = Offset(x = (smallDimen / 3f) + gap, y = 0f),
                    occupied = bases.onFirst,
                    occupiedColor = occupiedBaseColor,
                    emptyColor = emptyBaseColor,
                    size = maxSize,
                    strokeWidth = strokeWidth,
                )

                drawBase(
                    topLeft = Offset(x = 0f, y = (smallDimen / 3f) + gap),
                    occupied = bases.onThird,
                    occupiedColor = occupiedBaseColor,
                    emptyColor = emptyBaseColor,
                    size = maxSize,
                    strokeWidth = strokeWidth,
                )
                drawBase(
                    topLeft = Offset.Zero,
                    occupied = bases.onSecond,
                    occupiedColor = occupiedBaseColor,
                    emptyColor = emptyBaseColor,
                    size = maxSize,
                    strokeWidth = strokeWidth,
                )
            }
        }
    }

}


fun DrawScope.drawBase(
    topLeft: Offset,
    occupied: Boolean,
    occupiedColor: Color,
    emptyColor: Color,
    size: Size,
    strokeWidth: Float,
) {
    drawRect(
        topLeft = if (occupied) topLeft else Offset(
            topLeft.x + (strokeWidth / 2f),
            topLeft.y + (strokeWidth / 2f)
        ),
        color = if (occupied) occupiedColor else emptyColor,
        style = if (occupied) Fill else Stroke(width = strokeWidth),
        size = if (occupied) size / 3f else Size(
            width = ((size.width / 3f) - (strokeWidth)),
            height = ((size.height / 3f) - (strokeWidth))
        ),
    )
}

@Composable
@Preview(showBackground = true)
private fun PreviewBaseDisplay() {
    BasesDisplay(
        bases = Bases(onFirst = false, onSecond = true, onThird = false),
        modifier = Modifier
            .height(200.dp)
            .width(200.dp)
    )
}