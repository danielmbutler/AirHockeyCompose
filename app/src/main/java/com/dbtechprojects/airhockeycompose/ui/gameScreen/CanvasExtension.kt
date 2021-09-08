package com.dbtechprojects.airhockeycompose.ui.gameScreen

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.CanvasDrawScope
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

fun DrawScope.drawBorder(height: Float, width: Float) {
    val gameBorder = Path()
    val gameBorderRight = Path()

    gameBorder.apply {
        lineTo(width / 3.5f, 0f)
        lineTo(0f, 0f)
        lineTo(0f, height)
        lineTo(width / 3.5f, height)
    }

    drawPath(
        path = gameBorder,
        color = Color.Yellow,
        style = Stroke(
            width = 6.dp.toPx(),
        ),

        )
    // Right border
    drawLine(
        Color.Yellow,
        start = Offset(width, 0f),
        end = Offset(width, height),
        strokeWidth = 6.dp.toPx()
    )
    drawLine(
        Color.Yellow,
        start = Offset(width, 0f),
        end = Offset(width/1.5f, 0f),
        strokeWidth = 6.dp.toPx()
    )
    drawLine(
        Color.Yellow,
        start = Offset(width, height),
        end = Offset(width/1.5f, height),
        strokeWidth = 6.dp.toPx()
    )

    // center line
    drawLine(
        Color.White,
        start = Offset(0f, height/2),
        end = Offset(width, height/2),
        strokeWidth = 6.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
    )
    // center circle
    drawCircle(
        color = Color.White,
        radius = 200f,
        center = Offset(width/2, height/2),
        style = Stroke(width = 6f, pathEffect =PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f) )
    )

    // ball
    drawCircle(
        color = Color.Black,
        radius = 40f,
        center = Offset(width/2, height/2),

    )

    // pucks
    drawCircle(
        color = Color.Red,
        radius = 100f,
        center = Offset(width/2, 100f),
    )
    drawCircle(
        color = Color.Blue,
        radius = 100f,
        center = Offset(width/2, height - 100f),
    )
}