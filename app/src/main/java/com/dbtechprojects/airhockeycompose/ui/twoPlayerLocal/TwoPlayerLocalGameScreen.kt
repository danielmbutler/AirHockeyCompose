package com.dbtechprojects.airhockeycompose.ui.twoPlayerLocal

import android.graphics.Typeface
import android.util.Log
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
import com.dbtechprojects.airhockeycompose.ui.playerVCPU.GameMenu
import com.dbtechprojects.airhockeycompose.ui.shared.GameState
import com.dbtechprojects.airhockeycompose.ui.shared.drawGameBoard
import com.dbtechprojects.airhockeycompose.ui.shared.SharedGameFunctions
import com.dbtechprojects.airhockeycompose.ui.shared.SharedGameFunctions.getMovementConditions
import com.dbtechprojects.airhockeycompose.ui.shared.SharedGameFunctions.setMovementConditionsToFalse
import com.dbtechprojects.airhockeycompose.ui.twoPlayerLocal.PlayerPositionHelper.getPlayerOnePosition
import com.dbtechprojects.airhockeycompose.ui.twoPlayerLocal.PlayerPositionHelper.getPlayerTwoPosition

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

    // get list of movement conditions to loop through and change later
    val movementConditions = getMovementConditions(gameState)

    //condition to reset game
    if (gameState.player1GoalCount.value > 4 || gameState.player2GoalCount.value > 4) {
        setMovementConditionsToFalse(movementConditions)
        playerTwoOffsetX.value = gameState.playerTwoStartOffsetX.value
        playerTwoOffsetY.value = gameState.playerTwoStartOffsetY.value
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
            .contains(playerTwoOffsetX.value) &&
                Range.create(
                    gameState.ballMovementYAxis - 120,
                    gameState.ballMovementYAxis
                )
                    .contains(playerTwoOffsetY.value) ||
                //ball has hit top border // if ball is higher than the starting position of player 2 then we know its at the top
                gameState.ballMovementXAxis < (gameState.ballStartOffsetX.value - 150f) &&
                Range.create(
                    gameState.playerTwoStartOffsetY.value - 250f,
                    gameState.playerTwoStartOffsetY.value - 100f
                )
                    .contains(gameState.ballMovementYAxis) ||
                gameState.ballMovementXAxis > (gameState.ballStartOffsetX.value + 150f) &&
                Range.create(
                    gameState.playerTwoStartOffsetY.value - 250f,
                    gameState.playerTwoStartOffsetY.value - 100f
                )
                    .contains(gameState.ballMovementYAxis)

    val leftCollisionPlayerOne: Boolean =
        // ball has hit player left side
        Range.create(gameState.ballMovementXAxis - 100f, gameState.ballMovementXAxis + 200f)
            .contains(gameState.playerOneOffsetX.value) &&
                Range.create(gameState.ballMovementYAxis, gameState.ballMovementYAxis + 60f)
                    .contains(gameState.playerOneOffsetY.value) ||
                // ball has hit right border
                gameState.ballMovementXAxis > 805f


    // if ball is below center line and hits a border we want the direction of the ball to go towards the bottom
    // if ball is above center line and hits a border we want the direction of the ball to go towards the top
    val borderLeftCollisionUp =
        gameState.ballMovementXAxis > 805f && gameState.ballMovementYAxis < 500f
    val borderLeftCollisionDown =
        gameState.ballMovementXAxis > 805f && gameState.ballMovementYAxis > 600f
    val borderRightCollisionUp =
        gameState.ballMovementXAxis < 100f && gameState.ballMovementYAxis < 500f
    val borderRightCollisionDown =
        gameState.ballMovementXAxis < 100f && gameState.ballMovementYAxis > 600f

    Log.d(
        "border collisions",
        "borderLeftCollisionUp $borderLeftCollisionUp, borderLeftCollisionDown $borderLeftCollisionDown, " +
                "borderRightCollisionUp $borderRightCollisionUp, borderRightCollisionDown $borderRightCollisionDown"
    )

    val leftCollisionPlayerTwo: Boolean =

        // ball has hit player 2 leftside
        Range.create(
            gameState.ballMovementXAxis - 100f,
            gameState.ballMovementXAxis + 200f
        )
            .contains(playerTwoOffsetX.value) &&
                Range.create(gameState.ballMovementYAxis, gameState.ballMovementYAxis + 60f)
                    .contains(playerTwoOffsetY.value)
    val rightCollisionPlayer2: Boolean =

        // ball has hit player 2 rightside
        Range.create(
            gameState.ballMovementXAxis - 200f,
            gameState.ballMovementXAxis - 100f
        )
            .contains(playerTwoOffsetX.value) &&
                Range.create(gameState.ballMovementYAxis, gameState.ballMovementYAxis + 60f)
                    .contains(playerTwoOffsetY.value)

    val rightCollisionPlayerOne: Boolean =
        // ball has hit player right side side
        Range.create(gameState.ballMovementXAxis - 200f, gameState.ballMovementXAxis - 100f)
            .contains(gameState.playerOneOffsetX.value) &&
                Range.create(gameState.ballMovementYAxis, gameState.ballMovementYAxis + 60f)
                    .contains(gameState.playerOneOffsetY.value) ||
                // ball has hit left border
                gameState.ballMovementXAxis < 100f

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


    /*
    the below movement conditions are handled in TwoPlayerLocalState.kt , once one is triggered
    then we need to disable all over movement operations to ensure the correct movement for the ball occurs
     */

    if (player1goalCheck || player2goalCheck) {
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
    if (leftCollisionPlayerOne && !gameState.goalCollisionMovement.value) {
        setMovementConditionsToFalse(movementConditions)
        gameState.leftCollisionMovement.value = true
    }
    if (rightCollisionPlayerOne && !gameState.goalCollisionMovement.value) {
        setMovementConditionsToFalse(movementConditions)
        gameState.rightCollisionMovement.value = true
    }
    if (leftCollisionPlayerTwo && !gameState.goalCollisionMovement.value) {
        setMovementConditionsToFalse(movementConditions)
        gameState.leftCollisionPlayerTwoMovement.value = true
    }
    if (rightCollisionPlayer2 && !gameState.goalCollisionMovement.value) {
        setMovementConditionsToFalse(movementConditions)
        gameState.rightCollisionPlayerTwoMovement.value = true
    }
    if (borderLeftCollisionDown) gameState.downCollisionMovement.value = true
    if (borderLeftCollisionUp) gameState.upCollisionMovement.value = true
    if (borderRightCollisionDown) gameState.downCollisionMovement.value = true
    if (borderRightCollisionUp) gameState.upCollisionMovement.value = true


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
                        // this function can return null if touch event is not near player
                        playerTwoPosition?.x?.let { pos ->
                            if (pos > 0f) playerTwoOffsetX.value = pos
                        }
                        playerTwoPosition?.y?.let { pos ->
                            if (pos > 0f) playerTwoOffsetY.value = pos
                        }

                        // workout whether playerOne's position should be changed
                        val playerOnePosition = getPlayerOnePosition(
                            endGame = gameState.endGame,
                            motionEvent = it,
                            playerOneOffsetX = gameState.playerOneOffsetX,
                            playerOneOffsetY = gameState.playerOneOffsetY
                        )

                        // this function can return null if touch event is not near player
                        playerOnePosition?.x?.let { pos ->
                            if (pos > 0f) gameState.playerOneOffsetX.value = pos
                        }
                        playerOnePosition?.y?.let { pos ->
                            if (pos > 0f) gameState.playerOneOffsetY.value = pos
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
                    center = if (!gameState.endGame.value) Offset(gameState.ballMovementXAxis, gameState.ballMovementYAxis) else Offset(gameState.ballStartOffsetX.value, gameState.ballStartOffsetY.value),

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

