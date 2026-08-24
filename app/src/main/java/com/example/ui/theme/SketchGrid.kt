package com.example.ui.theme

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.sketchGridBackground(
    isDark: Boolean,
    gridSize: Dp = 22.dp
): Modifier = this.drawBehind {
    val step = gridSize.toPx()
    val width = size.width
    val height = size.height

    if (isDark) {
        // Dark Neon grid (~0.04 opacity subtle grid)
        val gridColor = Color(0xFF00E5FF).copy(alpha = 0.04f)
        var x = 0f
        while (x < width) {
            drawLine(
                color = gridColor,
                start = Offset(x, 0f),
                end = Offset(x, height),
                strokeWidth = 1f
            )
            x += step
        }
        var y = 0f
        while (y < height) {
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1f
            )
            y += step
        }
    } else {
        // Light Sketch graph paper grid (#E5E7EB thin grid lines)
        val gridColor = Color(0xFFE5E7EB)
        var x = 0f
        while (x < width) {
            drawLine(
                color = gridColor,
                start = Offset(x, 0f),
                end = Offset(x, height),
                strokeWidth = 1.2f
            )
            x += step
        }
        var y = 0f
        while (y < height) {
            drawLine(
                color = gridColor,
                start = Offset(0f, y),
                end = Offset(width, y),
                strokeWidth = 1.2f
            )
            y += step
        }
    }
}
