package com.dbtechprojects.airhockeycompose.ui.gameScreen

import android.util.Log
import android.util.Range
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
import androidx.compose.ui.graphics.*
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
    // setting out initial positions
    var playerOneStartOffsetX by remember { mutableStateOf(461f) }
    var playerOneStartOffsetY by remember { mutableStateOf(1780f) }
    var playerOneOffsetX by remember { mutableStateOf(461f) }
    var playerOneOffsetY by remember { mutableStateOf(1780f) }
    val playerTwoStartOffsetX by remember { mutableStateOf(461f) }
    val playerTwoStartOffsetY by remember { mutableStateOf(100f) }
    val ballStartOffsetX by remember { mutableStateOf(461f) }
    val ballStartOffsetY by remember { mutableStateOf(958f) }

    // defining collision types
    var upCollisionMovement by remember {
        mutableStateOf(false)
    }
    var downCollisionMovement by remember {
        mutableStateOf(false)
    }
    var leftCollisionMovement by remember {
        mutableStateOf(false)
    }
    var rightCollisionMovement by remember {
        mutableStateOf(false)
    }
    var goalCollisionMovement by remember {
        mutableStateOf(false)
    }
    var player1GoalCount by remember {
        mutableStateOf(0)
    }
    var player2GoalCount by remember {
        mutableStateOf(0)
    }
    var player1Goal by remember {
        mutableStateOf(false)
    }
    var player2Goal by remember {
        mutableStateOf(false)
    }


    val ballMovementYAxis by animateFloatAsState(
        targetValue =
        when {
            downCollisionMovement -> {
                ballStartOffsetY + 1850f
            }
            upCollisionMovement -> {
                ballStartOffsetY - 1700f
            }
            leftCollisionMovement -> {
                ballStartOffsetY - 900f
            }
            rightCollisionMovement -> {
                ballStartOffsetY - 900f
            }
            goalCollisionMovement -> {
                ballStartOffsetY
            }

            else -> ballStartOffsetY
        },
        animationSpec = tween(850, easing = LinearEasing),
        finishedListener = {
            if (goalCollisionMovement){
                goalCollisionMovement = false
                if(player1Goal) player1GoalCount += 1
                if (player2Goal) player2GoalCount += 1
            }
        }
    )
    val ballMovementXAxis by animateFloatAsState(
        targetValue =
        when {
            leftCollisionMovement -> {
                ballStartOffsetX - 650f
            }
            rightCollisionMovement -> {
                ballStartOffsetX + 650f
            }
            else -> ballStartOffsetX
        },
        animationSpec = tween(1000, easing = LinearEasing),

        )

    val player2MovementXAxis by animateFloatAsState(
        targetValue = when {
            gameModeState.value -> {
                ballMovementXAxis
            }
            else -> playerTwoStartOffsetX
        },
        animationSpec = tween(1500, easing = LinearEasing)
    )

    val player2MovementYAxis by animateFloatAsState(
        targetValue = when {
            downCollisionMovement -> {
                ballStartOffsetY // center line
            }
            else -> {
                playerTwoStartOffsetY
            }
        },
        animationSpec = tween(1500, easing = LinearEasing)
    )


    // define collision checks

    val upCollision: Boolean =
        // ball has hit player one
        Range.create(ballMovementXAxis - 100f, ballMovementXAxis + 100f)
            .contains(playerOneOffsetX) &&
                Range.create(ballMovementYAxis, ballMovementYAxis + 120f)
                    .contains(playerOneOffsetY)
    val downCollision: Boolean =
        // ball has hit player 2
        Range.create(ballMovementXAxis - 100f, ballMovementXAxis + 100f)
            .contains(player2MovementXAxis) &&
                Range.create(ballMovementYAxis - 80f, ballMovementYAxis + 150f)
                    .contains(player2MovementYAxis) ||
                //ball has hit top border
                ballMovementXAxis < (ballStartOffsetX - 150f) &&
                Range.create(playerTwoStartOffsetY - 100f, playerTwoStartOffsetY)
                    .contains(ballMovementYAxis) || // if ball is higher than the starting position of player 2 then we know its at the top
                ballMovementXAxis > (ballStartOffsetX + 150f) &&
                Range.create(playerTwoStartOffsetY - 100f, playerTwoStartOffsetY + 100f)
                    .contains(ballMovementYAxis)

    val leftCollision: Boolean =
        // ball has hit player left side
        Range.create(ballMovementXAxis - 100f, ballMovementXAxis + 200f)
            .contains(playerOneOffsetX) &&
                Range.create(ballMovementYAxis, ballMovementYAxis + 60f)
                    .contains(playerOneOffsetY) ||
                // ball has hit right border
                ballMovementXAxis > 805f ||
                // ball has hit player 2 leftside
                Range.create(ballMovementXAxis - 100f, ballMovementXAxis + 200f)
                    .contains(player2MovementXAxis) &&
                Range.create(ballMovementYAxis, ballMovementYAxis + 60f)
                    .contains(player2MovementYAxis)

    val rightCollision: Boolean =
        // ball has hit player right side side
        Range.create(ballMovementXAxis - 200f, ballMovementXAxis - 100f)
            .contains(playerOneOffsetX) &&
                Range.create(ballMovementYAxis, ballMovementYAxis + 60f)
                    .contains(playerOneOffsetY) ||
                // ball has hit left border
                ballMovementXAxis < 100f ||
                // ball has hit player 2 right side
                Range.create(ballMovementXAxis - 200f, ballMovementXAxis - 100f)
                    .contains(player2MovementXAxis) &&
                Range.create(ballMovementYAxis, ballMovementYAxis + 60f)
                    .contains(player2MovementXAxis)

    val player1goalCheck: Boolean =
        // top goal
        ballMovementYAxis < playerTwoStartOffsetY &&
                Range.create(playerTwoStartOffsetX - 50f, playerTwoStartOffsetX + 50f)
                    .contains(ballMovementXAxis)
        // bottom goal
    val player2goalCheck : Boolean =
        (ballMovementYAxis > playerOneStartOffsetY + 100f) &&
                Range.create(playerOneStartOffsetX - 50f, playerOneStartOffsetX + 50f)
                    .contains(ballMovementXAxis)
//

    Log.d(
        "GameBoard",
        "playerXOffset: $playerOneOffsetX playerYOffset: ${playerOneOffsetY}," +
                " ballOffsetX ${ballMovementXAxis}, ballOffsetY ${ballMovementYAxis}, collision: $upCollision"
    )
//    Log.d("GameBoard", "up collision : $upCollisionMovement, down collision $downCollisionMovement, right collision $rightCollisionMovement" +
//            "left collision : $leftCollisionMovement")
////    Log.d(
//        "GameBoard", "playerTwoXOffset: $playerTwoOffsetX playerTwoYOffset: ${playerTwoOffsetY}," +
//                " ballOffsetX ${ballOffsetX}, ballOffsetY ${ballOffsetY}, collision: $downCollision"
//    )

    if (player1goalCheck || player2goalCheck) {
        Log.d("Game Board", "GOALLLLLLLL $goalCollisionMovement")
        upCollisionMovement = false
        downCollisionMovement = false
        leftCollisionMovement = false
        rightCollisionMovement = false
        goalCollisionMovement = true
        if (player1goalCheck){
            player1Goal = true
            player2Goal = false
        }
        if(player2goalCheck){
            player2Goal = true
            player1Goal = false
        }
    }

    if (upCollision && !goalCollisionMovement) {
        upCollisionMovement = true
        downCollisionMovement = false
        leftCollisionMovement = false
        rightCollisionMovement = false

    }
    if (downCollision && !goalCollisionMovement) {
        downCollisionMovement = true
        upCollisionMovement = false
        leftCollisionMovement = false
        rightCollisionMovement = false
    }
    if (leftCollision && !goalCollisionMovement) {
        upCollisionMovement = false
        downCollisionMovement = false
        rightCollisionMovement = false
        leftCollisionMovement = true
    }
    if (rightCollision && !goalCollisionMovement) {
        leftCollisionMovement = false
        upCollisionMovement = false
        downCollisionMovement = false
        rightCollisionMovement = true
    }


    Box(
        Modifier
            .border(6.dp, color = Color.White, shape = RectangleShape)
            .background(Color.Gray)
            .fillMaxSize()
    )
    {

        Column() {
            // paint object o use to write scores

            val paint = Paint().asFrameworkPaint()
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
                                    .create(
                                        playerOneOffsetX - 100f,
                                        playerOneOffsetX + 100f
                                    )
                                    .contains(change.position.x) && Range
                                    .create(
                                        playerOneOffsetY - 100f,
                                        playerOneOffsetY + 100f
                                    )
                                    .contains(change.position.y)
                            ) {
                                if (playerOneOffsetX < 805f && playerOneOffsetX > 160f) {
                                    //player is within boundary so update location
                                    playerOneOffsetX += dragAmount.x
                                } else if (playerOneOffsetX > 805f && dragAmount.x < 0) {
                                    // player is almost at the right most edge of boundary so only accept drags to the left
                                    playerOneOffsetX += dragAmount.x
                                } else if (playerOneOffsetX < 160f && dragAmount.x > 0) {
                                    // player is almost at the left most edge of boundary so only accept drags to the right
                                    playerOneOffsetX += dragAmount.x
                                }

                                // handle Vertical dragging
                                if (playerOneOffsetY > 1050f && playerOneOffsetY < 1790f) {
                                    playerOneOffsetY += dragAmount.y
                                } else if (playerOneOffsetY > 1790f && dragAmount.y < 0) {
                                    playerOneOffsetY += dragAmount.y
                                } else if (playerOneOffsetY < 1050f && dragAmount.y > 0) {
                                    playerOneOffsetY += dragAmount.y
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
                    center = Offset(ballMovementXAxis, ballMovementYAxis),

                    )
                // pucks
                drawCircle(
                    color = Color.Red,
                    radius = 100f,
                    center = Offset(player2MovementXAxis, player2MovementYAxis),
                )
                Log.d("player 2 pos", "x: ${width / 2} y: 100f")
                //Offset(width/2, height - 100f),
                drawCircle(
                    color = Color.Blue,
                    radius = 100f,
                    center = Offset(playerOneOffsetX, playerOneOffsetY)
                )

                // scores
                paint.apply {
                    isAntiAlias = true
                    textSize = 100f
                    typeface = android.graphics.Typeface.DEFAULT
                }

                drawContext.canvas.nativeCanvas.drawText(
                    player2GoalCount.toString(),
                    100f,
                    height / 2 - 200f,
                    paint
                )
                drawContext.canvas.nativeCanvas.drawText(
                    player1GoalCount.toString(),
                    100f,
                    height / 2 + 200f,
                    paint
                )

            }
        }
        if (!gameModeState.value) {
            GameMenu {
                gameModeState.value = true
            }
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
