package com.dbtechprojects.airhockeycompose.ui.gameScreen.playerVCPU

import android.graphics.Typeface
import android.util.Log
import android.util.Range
import androidx.activity.ComponentActivity
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
import androidx.compose.ui.zIndex
import com.dbtechprojects.airhockeycompose.ui.gameScreen.shared.drawGameBoard
import com.dbtechprojects.airhockeycompose.ui.gameScreen.shared.sharedGameFunctions

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
fun GameBoard(menuState: MutableState<Boolean>, gameState: GameState) {
    // setting out initial positions

    if (gameState.player1GoalCount.value > 4 || gameState.player2GoalCount.value > 4) {
        gameState.endGame.value = true
        gameState.downCollisionMovement.value = false
        gameState.goalCollisionMovement.value = false
        gameState.rightCollisionMovement.value = false
        gameState.leftCollisionMovement.value = false
        gameState.upCollisionMovement.value = false
    }

    val ballMovementYAxis by animateFloatAsState(
        targetValue =
        when {
            gameState.downCollisionMovement.value -> {
                gameState.ballStartOffsetY.value + 1850f
            }
            gameState.upCollisionMovement.value -> {
                gameState.ballStartOffsetY.value - 1700f
            }
            gameState.leftCollisionMovement.value -> {
                gameState.ballStartOffsetY.value - 900f
            }
            gameState.rightCollisionMovement.value -> {
                gameState.ballStartOffsetY.value - 900f
            }
            gameState.goalCollisionMovement.value -> {
                gameState.ballStartOffsetY.value
            }

            else -> gameState.ballStartOffsetY.value
        },
        animationSpec = tween(850, easing = LinearEasing),
        finishedListener = {
            if (gameState.goalCollisionMovement.value) {
                gameState.goalCollisionMovement.value = false
                if (gameState.player1Goal.value) gameState.player1GoalCount.value += 1
                if (gameState.player2Goal.value) gameState.player2GoalCount.value += 1
            }
        }
    )
    val ballMovementXAxis by animateFloatAsState(
        targetValue =
        when {
            gameState.leftCollisionMovement.value -> {
                gameState.ballStartOffsetX.value - 650f
            }
            gameState.rightCollisionMovement.value -> {
                gameState.ballStartOffsetX.value + 650f
            }
            else -> gameState.ballStartOffsetX.value
        },
        animationSpec = tween(1000, easing = LinearEasing),

        )

    val player2MovementXAxis by animateFloatAsState(
        targetValue = when {
            menuState.value -> {
                ballMovementXAxis
            }
            else -> gameState.playerTwoStartOffsetX.value
        },
        animationSpec = tween(1500, easing = LinearEasing)
    )

    val player2MovementYAxis by animateFloatAsState(
        targetValue = when {
            gameState.downCollisionMovement.value -> {
                gameState.ballStartOffsetY.value // center line
            }
            else -> {
                gameState.playerTwoStartOffsetY.value
            }
        },
        animationSpec = tween(1500, easing = LinearEasing)
    )


    // define collision checks

    val upCollision: Boolean =
        // ball has hit player one
        Range.create(ballMovementXAxis - 100f, ballMovementXAxis + 100f)
            .contains(gameState.playerOneOffsetX.value) &&
                Range.create(ballMovementYAxis, ballMovementYAxis + 120f)
                    .contains(gameState.playerOneOffsetY.value)
    val downCollision: Boolean =
        // ball has hit player 2
        Range.create(ballMovementXAxis - 100f, ballMovementXAxis + 100f)
            .contains(player2MovementXAxis) &&
                Range.create(ballMovementYAxis - 80f, ballMovementYAxis + 150f)
                    .contains(player2MovementYAxis) ||
                //ball has hit top border
                ballMovementXAxis < (gameState.ballStartOffsetX.value - 150f) &&
                Range.create(
                    gameState.playerTwoStartOffsetY.value - 100f,
                    gameState.playerTwoStartOffsetY.value
                )
                    .contains(ballMovementYAxis) || // if ball is higher than the starting position of player 2 then we know its at the top
                ballMovementXAxis > (gameState.ballStartOffsetX.value + 150f) &&
                Range.create(
                    gameState.playerTwoStartOffsetY.value - 100f,
                    gameState.playerTwoStartOffsetY.value + 100f
                )
                    .contains(ballMovementYAxis)

    val leftCollision: Boolean =
        // ball has hit player left side
        Range.create(ballMovementXAxis - 100f, ballMovementXAxis + 200f)
            .contains(gameState.playerOneOffsetX.value) &&
                Range.create(ballMovementYAxis, ballMovementYAxis + 60f)
                    .contains(gameState.playerOneOffsetY.value) ||
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
            .contains(gameState.playerOneOffsetX.value) &&
                Range.create(ballMovementYAxis, ballMovementYAxis + 60f)
                    .contains(gameState.playerOneOffsetY.value) ||
                // ball has hit left border
                ballMovementXAxis < 100f ||
                // ball has hit player 2 right side
                Range.create(ballMovementXAxis - 200f, ballMovementXAxis - 100f)
                    .contains(player2MovementXAxis) &&
                Range.create(ballMovementYAxis, ballMovementYAxis + 60f)
                    .contains(player2MovementXAxis)

    val player1goalCheck: Boolean =
        // top goal
        ballMovementYAxis < gameState.playerTwoStartOffsetY.value &&
                Range.create(
                    gameState.playerTwoStartOffsetX.value - 50f,
                    gameState.playerTwoStartOffsetX.value + 50f
                )
                    .contains(ballMovementXAxis)
    // bottom goal
    val player2goalCheck: Boolean =
        (ballMovementYAxis > gameState.playerOneStartOffsetY.value + 100f) &&
                Range.create(
                    gameState.playerOneStartOffsetX.value - 50f,
                    gameState.playerOneStartOffsetX.value + 50f
                )
                    .contains(ballMovementXAxis)
//

//    Log.d(
//        "GameBoard",
//        "playerXOffset: $playerOneOffsetX playerYOffset: ${playerOneOffsetY}," +
//                " ballOffsetX ${ballMovementXAxis}, ballOffsetY ${ballMovementYAxis}, collision: $upCollision"
//    )
//    Log.d("GameBoard", "up collision : $upCollisionMovement, down collision $downCollisionMovement, right collision $rightCollisionMovement" +
//            "left collision : $leftCollisionMovement")
////    Log.d(
//        "GameBoard", "playerTwoXOffset: $playerTwoOffsetX playerTwoYOffset: ${playerTwoOffsetY}," +
//                " ballOffsetX ${ballOffsetX}, ballOffsetY ${ballOffsetY}, collision: $downCollision"
//    )

    if (player1goalCheck || player2goalCheck) {
//        Log.d("Game Board", "GOALLLLLLLL $goalCollisionMovement")
        gameState.upCollisionMovement.value = false
        gameState.downCollisionMovement.value = false
        gameState.leftCollisionMovement.value = false
        gameState.rightCollisionMovement.value = false
        gameState.goalCollisionMovement.value = true
        if (player1goalCheck) {
            gameState.player1Goal.value = true
            gameState.player2Goal.value = false
        }
        if (player2goalCheck) {
            gameState.player2Goal.value = true
            gameState.player1Goal.value = false
        }
    }

    if (upCollision && !gameState.goalCollisionMovement.value) {
        gameState.upCollisionMovement.value = true
        gameState.downCollisionMovement.value = false
        gameState.leftCollisionMovement.value = false
        gameState.rightCollisionMovement.value = false

    }
    if (downCollision && !gameState.goalCollisionMovement.value) {
        gameState.downCollisionMovement.value = true
        gameState.upCollisionMovement.value = false
        gameState.leftCollisionMovement.value = false
        gameState.rightCollisionMovement.value = false
    }
    if (leftCollision && !gameState.goalCollisionMovement.value) {
        gameState.upCollisionMovement.value = false
        gameState.downCollisionMovement.value = false
        gameState.rightCollisionMovement.value = false
        gameState.leftCollisionMovement.value = true
    }
    if (rightCollision && !gameState.goalCollisionMovement.value) {
        gameState.leftCollisionMovement.value = false
        gameState.upCollisionMovement.value = false
        gameState.downCollisionMovement.value = false
        gameState.rightCollisionMovement.value = true
    }


    Box(
        Modifier
            .border(6.dp, color = Color.White, shape = RectangleShape)
            .background(Color.Gray)
            .fillMaxSize()
    )
    {
        if (gameState.endGame.value) {
            Button(
                onClick = {
                    menuState.value = false
                    gameState.endGame.value = false
                    gameState.player1GoalCount.value = 0
                    gameState.player2GoalCount.value = 0
                }, modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 180.dp)
                    .zIndex(1f)
            ) {
                Text(text = "Return to Menu")
            }


        }

        Column(modifier = Modifier.zIndex(-1f)) {
            // paint object to use to write scores
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
                                        gameState.playerOneOffsetX.value - 100f,
                                        gameState.playerOneOffsetX.value + 100f
                                    )
                                    .contains(change.position.x) && Range
                                    .create(
                                        gameState.playerOneOffsetY.value - 100f,
                                        gameState.playerOneOffsetY.value + 100f
                                    )
                                    .contains(change.position.y) && !gameState.endGame.value
                            ) {
                                if (gameState.playerOneOffsetX.value < 805f && gameState.playerOneOffsetX.value > 160f) {
                                    //player is within boundary so update location
                                    gameState.playerOneOffsetX.value += dragAmount.x
                                } else if (gameState.playerOneOffsetX.value > 805f && dragAmount.x < 0) {
                                    // player is almost at the right most edge of boundary so only accept drags to the left
                                    gameState.playerOneOffsetX.value += dragAmount.x
                                } else if (gameState.playerOneOffsetX.value < 160f && dragAmount.x > 0) {
                                    // player is almost at the left most edge of boundary so only accept drags to the right
                                    gameState.playerOneOffsetX.value += dragAmount.x
                                }

                                // handle Vertical dragging
                                if (gameState.playerOneOffsetY.value > 1050f && gameState.playerOneOffsetY.value < 1790f) {
                                    gameState.playerOneOffsetY.value += dragAmount.y
                                } else if (gameState.playerOneOffsetY.value > 1790f && dragAmount.y < 0) {
                                    gameState.playerOneOffsetY.value += dragAmount.y
                                } else if (gameState.playerOneOffsetY.value < 1050f && dragAmount.y > 0) {
                                    gameState.playerOneOffsetY.value += dragAmount.y
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
                    center = Offset(
                        gameState.playerOneOffsetX.value,
                        gameState.playerOneOffsetY.value
                    )
                )

                // scores
                paint.apply {
                    isAntiAlias = true
                    textSize = 100f
                    typeface = Typeface.DEFAULT
                }

                drawContext.canvas.nativeCanvas.drawText(
                    gameState.player2GoalCount.value.toString(),
                    100f,
                    height / 2 - 200f,
                    paint
                )
                drawContext.canvas.nativeCanvas.drawText(
                    gameState.player1GoalCount.value.toString(),
                    100f,
                    height / 2 + 200f,
                    paint
                )

                if (gameState.endGame.value) {
                    drawContext.canvas.nativeCanvas.drawText(
                        "Game Over ${
                            sharedGameFunctions.determineWinner(
                                gameState.player1GoalCount.value,
                                gameState.player2GoalCount.value
                            )
                        }",
                        150f,
                        height / 2 + 400f,
                        paint.apply {
                            textSize = 50f
                            typeface = Typeface.DEFAULT_BOLD
                            color = Color.Yellow.toArgb()
                        }
                    )

                }

            }
        }
        if (!menuState.value) {
            GameMenu {
                menuState.value = true
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
@Composable
fun ComponentActivity.playerVsCpuState() : GameState {
    return GameState(
        playerOneOffsetX = remember { mutableStateOf(461f) },
        playerOneStartOffsetY = remember { mutableStateOf(1780f) },
        playerOneStartOffsetX = remember { mutableStateOf(461f) },
        playerOneOffsetY = remember { mutableStateOf(1780f) },
        playerTwoStartOffsetX = remember { mutableStateOf(461f) },
        playerTwoStartOffsetY = remember { mutableStateOf(100f) },
        ballStartOffsetX = remember { mutableStateOf(461f) },
        ballStartOffsetY = remember { mutableStateOf(958f) },
        upCollisionMovement = remember { mutableStateOf(false) },
        downCollisionMovement = remember { mutableStateOf(false) },
        goalCollisionMovement = remember { mutableStateOf(false) },
        rightCollisionMovement = remember { mutableStateOf(false) },
        leftCollisionMovement = remember { mutableStateOf(false) },
        player1Goal = remember { mutableStateOf(false) },
        player2Goal = remember {
            mutableStateOf(false)
        },
        player1GoalCount = remember { mutableStateOf(0) },
        player2GoalCount = remember { mutableStateOf(0) },
        endGame = remember { mutableStateOf(false) }


    )
}