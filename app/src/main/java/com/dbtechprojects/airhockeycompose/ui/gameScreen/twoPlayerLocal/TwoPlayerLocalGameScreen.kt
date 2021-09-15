package com.dbtechprojects.airhockeycompose.ui.gameScreen.twoPlayerLocal

import android.graphics.Typeface
import android.util.Range
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.*
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import com.dbtechprojects.airhockeycompose.ui.gameScreen.playerVCPU.GameMenu
import com.dbtechprojects.airhockeycompose.ui.gameScreen.playerVCPU.GameState
import com.dbtechprojects.airhockeycompose.ui.gameScreen.shared.drawGameBoard
import com.dbtechprojects.airhockeycompose.ui.gameScreen.shared.SharedGameFunctions
import com.dbtechprojects.airhockeycompose.ui.gameScreen.twoPlayerLocal.PlayerPositionHelper.getPlayerOnePosition
import com.dbtechprojects.airhockeycompose.ui.gameScreen.twoPlayerLocal.PlayerPositionHelper.getPlayerTwoPosition

@ExperimentalComposeUiApi
@Composable
fun TwoPlayerGameBoard(
    playerVsCpuState: () -> Unit,
    gameState: GameState,
    twoPlayerLocal: () -> Unit,
) {
    // setting out initial positions

    val playerTwoOffsetX = remember { mutableStateOf(gameState.playerTwoOffsetX) }
    val playerTwoOffsetY = remember { mutableStateOf(gameState.playerTwoOffsetY) }

    if (gameState.player1GoalCount.value > 4 || gameState.player2GoalCount.value > 4) {
        gameState.endGame.value = true
        gameState.downCollisionMovement.value = false
        gameState.goalCollisionMovement.value = false
        gameState.rightCollisionMovement.value = false
        gameState.leftCollisionMovement.value = false
        gameState.upCollisionMovement.value = false
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
                    .pointerInteropFilter {

                        // workout whether playerOne's position should be changed
                        val playerTwoPosition = getPlayerTwoPosition(
                            endGame = gameState.endGame,
                            motionEvent = it,
                            playerTwoOffsetX = playerTwoOffsetX,
                            playerTwoOffsetY = playerTwoOffsetY
                        )

                        playerTwoPosition?.x?.let { pos ->
                            if(pos > 0f) playerTwoOffsetX.value = pos
                        }
                        playerTwoPosition?.y?.let { pos ->
                            if(pos > 0f) playerTwoOffsetY.value = pos
                        }

                        // workout whether playerOne's position should be changed
                        val playerOnePosition = getPlayerOnePosition(
                            endGame = gameState.endGame,
                            motionEvent = it,
                            playerOneOffsetX = gameState.playerOneOffsetX,
                            playerOneOffsetY = gameState.playerOneOffsetY
                        )

                        playerOnePosition?.x?.let { pos ->
                            if(pos > 0f) gameState.playerOneOffsetX.value = pos
                        }
                        playerOnePosition?.y?.let { pos ->
                            if(pos > 0f) gameState.playerOneOffsetY.value = pos
                        }

                        true
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
                    center = Offset(
                        playerTwoOffsetX.value,
                        playerTwoOffsetY.value
                    )
                )

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
        if (!gameState.menuState.value) {
            GameMenu(
                playerVsCpuState = playerVsCpuState,
                twoPlayerLocal = twoPlayerLocal,
                onGameButtonClick = { gameState.menuState.value = true })
        }
    }

}

