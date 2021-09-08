package com.dbtechprojects.airhockeycompose.ui.gameScreen

import android.util.Log
import android.util.Range
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
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
fun GameBoard(gameModeState: MutableState<Boolean>) {
    var playerOffsetX by remember { mutableStateOf(450f) }
    var playerOffsetY by remember { mutableStateOf(1780f) }
    var ballOffsetX by remember { mutableStateOf(461f) }
    var ballOffsetY by remember { mutableStateOf(958f) }
    var collision by remember {
        mutableStateOf(false)
    }

    val hasPlayerHitBall: Boolean =
        Range.create(ballOffsetX - 150f, ballOffsetX + 150f).contains(playerOffsetX) &&
                Range.create(ballOffsetY - 150f, ballOffsetY + 150f).contains(playerOffsetY)

    Log.d(
        "GameBoard", "playerXOffset: $playerOffsetX playerYOffset: ${playerOffsetY}," +
                " ballOffsetX ${ballOffsetX}, ballOffsetY ${ballOffsetY}, collision: $hasPlayerHitBall"
    )

    if (hasPlayerHitBall){
        collision = true
    }
    val ballMovementYAxis by animateFloatAsState(
        //if game has not started or the game is in reverse mode then move enemies back to enemy box
        targetValue =  if (!collision) {
            ballOffsetY
        } else {
            ballOffsetY - 750f
        },
        animationSpec = tween(4000, easing = LinearEasing),
        finishedListener = {
        }
    )

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
                                        .create(playerOffsetX - 100f, playerOffsetX + 100f)
                                        .contains(change.position.x) && Range
                                        .create(
                                            playerOffsetY - 100f,
                                            playerOffsetY + 100f
                                        )
                                        .contains(change.position.y)
                                ) {
                                    if (playerOffsetX < 805f && playerOffsetX > 160f) {
                                        //player is within boundary so update location
                                        playerOffsetX += dragAmount.x
                                    } else if (playerOffsetX > 805f && dragAmount.x < 0) {
                                        // player is almost at the right most edge of boundary so only accept drags to the left
                                        playerOffsetX += dragAmount.x
                                    } else if (playerOffsetX < 160f && dragAmount.x > 0) {
                                        // player is almost at the left most edge of boundary so only accept drags to the right
                                        playerOffsetX += dragAmount.x
                                    }

                                    // handle Vertical dragging
                                    if (playerOffsetY > 1050f && playerOffsetY < 1790f) {
                                        playerOffsetY += dragAmount.y
                                    } else if (playerOffsetY > 1790f && dragAmount.y < 0) {
                                        playerOffsetY += dragAmount.y
                                    } else if (playerOffsetY < 1050f && dragAmount.y > 0) {
                                        playerOffsetY += dragAmount.y
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
                        center = Offset(ballOffsetX, ballMovementYAxis),

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
                        center = Offset(playerOffsetX, playerOffsetY)
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
