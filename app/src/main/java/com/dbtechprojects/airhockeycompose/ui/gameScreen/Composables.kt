package com.dbtechprojects.airhockeycompose.ui.gameScreen


import android.util.Log
import android.util.Range
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameTitle(text: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun GameBorder(gameModeState: MutableState<Boolean>) {
    var offsetX by remember { mutableStateOf(450f) }
    var offsetY by remember { mutableStateOf(1780f) }

    Box(
        Modifier
            .border(6.dp, color = Color.White, shape = RectangleShape)
            .background(Color.Gray)
            .fillMaxSize()
    )
    {

        Column() {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
                    .pointerInput(Unit) {
                        /*
                        handle dragging player counter, the below logic keeps the playing within the game
                        boundaries, as the values are mutable we are continuously keeping track of them and will update
                        the players location unless the drag event is within the boundary
                         */
                        detectDragGestures { change, dragAmount ->
                            change.consumeAllChanges()
                            // these range conditions confirm that the finger is placed within 100f of the players puck
                            if (Range
                                    .create(offsetX - 100f, offsetX + 100f)
                                    .contains(change.position.x) && Range
                                    .create(
                                        offsetY - 100f,
                                        offsetY + 100f
                                    )
                                    .contains(change.position.y)
                            ) {
                                if (offsetX < 805f && offsetX > 160f) {
                                    //player is within boundary so update location
                                    offsetX += dragAmount.x
                                } else if (offsetX > 805f && dragAmount.x < 0) {
                                    // player is almost at the right most edge of boundary so only accept drags to the left
                                    offsetX += dragAmount.x
                                } else if (offsetX < 160f && dragAmount.x > 0) {
                                    // player is almost at the left most edge of boundary so only accept drags to the right
                                    offsetX += dragAmount.x
                                }

                                // handle Vertical dragging
                                if (offsetY > 1050f && offsetY < 1790f) {
                                    offsetY += dragAmount.y
                                }
                                else if (offsetY > 1790f && dragAmount.y < 0) {
                                    offsetY += dragAmount.y
                                }
                                else if (offsetY < 1050f && dragAmount.y > 0) {
                                    offsetY += dragAmount.y
                                }

                            }
                        }
                    }
            ) {
                val height = this.size.height
                val width = this.size.width

                drawGameBoard(height, width)

                // ball
                drawCircle(
                    color = Color.Black,
                    radius = 40f,
                    center = Offset(width / 2, height / 2),

                    )
                // pucks
                drawCircle(
                    color = Color.Red,
                    radius = 100f,
                    center = Offset(width / 2, 100f),
                )
                //Offset(width/2, height - 100f),
                drawCircle(
                    color = Color.Blue,
                    radius = 100f,
                    center = Offset(offsetX, offsetY)
                )
            }
        }
        if (!gameModeState.value) {
            GameMenu { gameModeState.value = true }
        }
    }
}

@Composable
fun GameMenu(onGameButtonClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .background(color = Color(0f, 0f, 0f, 0.6f), shape = RectangleShape)
    ) {
        GameTitle(text = "Air Hockey Compose")
        Row(
            Modifier.fillMaxSize(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column() {

                Button(
                    onClick = { onGameButtonClick.invoke() },
                    Modifier
                        .padding(10.dp)
                        .width(180.dp)
                ) {
                    Text(text = "Single Player Local")
                }
                Button(
                    onClick = { onGameButtonClick.invoke() },
                    Modifier
                        .padding(10.dp)
                        .width(180.dp)
                ) {
                    Text(text = "Two Player Local")
                }
                Button(
                    onClick = { onGameButtonClick.invoke() },
                    Modifier
                        .padding(10.dp)
                        .width(180.dp)
                ) {
                    Text(text = "Two Player Online")
                }
            }

        }
    }
}
