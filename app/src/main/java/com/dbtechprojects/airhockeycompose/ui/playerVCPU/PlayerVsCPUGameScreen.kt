package com.dbtechprojects.airhockeycompose.ui.playerVCPU

import android.graphics.Typeface
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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.consumeAllChanges
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.dbtechprojects.airhockeycompose.ui.shared.GameState
import com.dbtechprojects.airhockeycompose.ui.shared.drawGameBoard
import com.dbtechprojects.airhockeycompose.ui.shared.SharedGameFunctions
import com.dbtechprojects.airhockeycompose.ui.shared.SharedGameFunctions.setMovementConditionsToFalse


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
fun GameBoard(gameState: GameState,) {


    // get list of movement conditions to loop through and change later
    val movementConditions = SharedGameFunctions.getMovementConditions(gameState)

    if (gameState.player1GoalCount.value > 4 || gameState.player2GoalCount.value > 4) {
        setMovementConditionsToFalse(movementConditions)
        gameState.playerOneOffsetY.value = gameState.playerOneStartOffsetY.value
        gameState.playerOneOffsetX.value = gameState.playerOneStartOffsetX.value
        gameState.endGame.value = true
    }

    // define collision checks

    val upCollision: Boolean =
        // ball has hit player one
        Range.create(gameState.ballMovementXAxis - 100f, gameState.ballMovementXAxis + 100f)
            .contains(gameState.playerOneOffsetX.value) &&
                Range.create(gameState.ballMovementYAxis, gameState.ballMovementYAxis + 120f)
                    .contains(gameState.playerOneOffsetY.value)
    val downCollision: Boolean =
        // ball has hit player 2
        Range.create(gameState.ballMovementXAxis - 100f, gameState.ballMovementXAxis + 100f)
            .contains(gameState.playerTwoOffsetX) &&
                Range.create(
                    gameState.ballMovementYAxis - 80f,
                    gameState.ballMovementYAxis + 150f
                )
                    .contains(gameState.playerTwoOffsetY) ||
                //ball has hit top border
                gameState.ballMovementXAxis < (gameState.ballStartOffsetX.value - 150f) &&
                Range.create(
                    gameState.playerTwoStartOffsetY.value - 100f,
                    gameState.playerTwoStartOffsetY.value
                )
                    .contains(gameState.ballMovementYAxis) || // if ball is higher than the starting position of player 2 then we know its at the top
                gameState.ballMovementXAxis > (gameState.ballStartOffsetX.value + 150f) &&
                Range.create(
                    gameState.playerTwoStartOffsetY.value - 100f,
                    gameState.playerTwoStartOffsetY.value + 100f
                )
                    .contains(gameState.ballMovementYAxis)

    val leftCollision: Boolean =
        // ball has hit player left side
        Range.create(gameState.ballMovementXAxis - 100f, gameState.ballMovementXAxis + 200f)
            .contains(gameState.playerOneOffsetX.value) &&
                Range.create(gameState.ballMovementYAxis, gameState.ballMovementYAxis + 60f)
                    .contains(gameState.playerOneOffsetY.value) ||
                // ball has hit right border
                gameState.ballMovementXAxis > 805f ||
                // ball has hit player 2 leftside
                Range.create(
                    gameState.ballMovementXAxis - 100f,
                    gameState.ballMovementXAxis + 200f
                )
                    .contains(gameState.playerTwoOffsetX) &&
                Range.create(gameState.ballMovementYAxis, gameState.ballMovementYAxis + 60f)
                    .contains(gameState.playerTwoOffsetY)

    val rightCollision: Boolean =
        // ball has hit player right side side
        Range.create(gameState.ballMovementXAxis - 200f, gameState.ballMovementXAxis - 100f)
            .contains(gameState.playerOneOffsetX.value) &&
                Range.create(gameState.ballMovementYAxis, gameState.ballMovementYAxis + 60f)
                    .contains(gameState.playerOneOffsetY.value) ||
                // ball has hit left border
                gameState.ballMovementXAxis < 100f ||
                // ball has hit player 2 right side
                Range.create(
                    gameState.ballMovementXAxis - 200f,
                    gameState.ballMovementXAxis - 100f
                )
                    .contains(gameState.playerTwoOffsetX) &&
                Range.create(gameState.ballMovementYAxis, gameState.ballMovementYAxis + 60f)
                    .contains(gameState.playerTwoOffsetY)

    val player1goalCheck: Boolean =
        // top goal
        gameState.ballMovementYAxis < gameState.playerTwoStartOffsetY.value &&
                Range.create(
                    gameState.playerTwoStartOffsetX.value - 50f,
                    gameState.playerTwoStartOffsetX.value + 50f
                )
                    .contains(gameState.ballMovementXAxis)
    // bottom goal
    val player2goalCheck: Boolean =
        (gameState.ballMovementYAxis > gameState.playerOneStartOffsetY.value + 100f) &&
                Range.create(
                    gameState.playerOneStartOffsetX.value - 50f,
                    gameState.playerOneStartOffsetX.value + 50f
                )
                    .contains(gameState.ballMovementXAxis)
//

//    Log.d(
//        "GameBoard",
//        "playerXOffset: $playerOneOffsetX playerYOffset: ${playerOneOffsetY}," +
//                " ballOffsetX ${gameState.ballMovementXAxis}, ballOffsetY ${gameState.ballMovementYAxis}, collision: $upCollision"
//    )
//    Log.d("GameBoard", "up collision : $upCollisionMovement, down collision $downCollisionMovement, right collision $rightCollisionMovement" +
//            "left collision : $leftCollisionMovement")
////    Log.d(
//        "GameBoard", "playerTwoXOffset: $playerTwoOffsetX playerTwoYOffset: ${playerTwoOffsetY}," +
//                " ballOffsetX ${ballOffsetX}, ballOffsetY ${ballOffsetY}, collision: $downCollision"
//    )

    if (player1goalCheck || player2goalCheck) {
//        Log.d("Game Board", "GOALLLLLLLL $goalCollisionMovement")
        setMovementConditionsToFalse(movementConditions)
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
        setMovementConditionsToFalse(movementConditions)
        gameState.upCollisionMovement.value = true

    }
    if (downCollision && !gameState.goalCollisionMovement.value) {
        setMovementConditionsToFalse(movementConditions)
        gameState.downCollisionMovement.value = true
    }
    if (leftCollision && !gameState.goalCollisionMovement.value) {
        setMovementConditionsToFalse(movementConditions)
        gameState.leftCollisionMovement.value = true
    }
    if (rightCollision && !gameState.goalCollisionMovement.value) {
        setMovementConditionsToFalse(movementConditions)
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
                    gameState.menuState.value = false
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
                        handle dragging player counter, the below logic keeps the player within the game
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
                    center = Offset(gameState.ballMovementXAxis, gameState.ballMovementYAxis),

                    )
                // pucks
                drawCircle(
                    color = Color.Red,
                    radius = 100f,
                    center = Offset(gameState.playerTwoOffsetX, gameState.playerTwoOffsetY),
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
                            SharedGameFunctions.determineWinner(
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

    }

}

@Composable
fun GameMenu(
    playerVsCpuState: () -> Unit,
    onGameButtonClick: () -> Unit,
    twoPlayerLocal: () -> Unit,
    twoPlayerOnline: () -> Unit
) {

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
                    onClick = {
                        playerVsCpuState.invoke()
                        onGameButtonClick.invoke()
                    },
                    Modifier
                        .padding(10.dp)
                        .width(180.dp)
                ) {
                    Text(text = "Single Player Local")
                }
                Button(
                    onClick = {
                        twoPlayerLocal.invoke()
                        onGameButtonClick.invoke()
                    },
                    Modifier
                        .padding(10.dp)
                        .width(180.dp)
                ) {
                    Text(text = "Two Player Local")
                }
                Button(
                    onClick = {
                        twoPlayerOnline.invoke()
                        onGameButtonClick.invoke()
                              },
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
