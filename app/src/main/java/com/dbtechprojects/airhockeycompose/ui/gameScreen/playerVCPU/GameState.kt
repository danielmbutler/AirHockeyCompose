package com.dbtechprojects.airhockeycompose.ui.gameScreen.playerVCPU

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.*

// creates base game state within constructor
data class GameState(
    val playerOneStartOffsetX: MutableState<Float> = mutableStateOf(461f),
    val playerOneStartOffsetY: MutableState<Float> = mutableStateOf(1780f),
    val playerOneOffsetX: MutableState<Float> = mutableStateOf(461f),
    val playerOneOffsetY: MutableState<Float> = mutableStateOf(1780f),
    val playerTwoStartOffsetX: MutableState<Float> = mutableStateOf(461f),
    val playerTwoStartOffsetY: MutableState<Float> = mutableStateOf(100f),
    val ballStartOffsetX : MutableState<Float> = mutableStateOf(461f),
    val ballStartOffsetY : MutableState<Float> = mutableStateOf(958f),
    val upCollisionMovement: MutableState<Boolean> = mutableStateOf(false),
    val downCollisionMovement: MutableState<Boolean> = mutableStateOf(false),
    val leftCollisionMovement: MutableState<Boolean> = mutableStateOf(false),
    val rightCollisionMovement: MutableState<Boolean> = mutableStateOf(false),
    val goalCollisionMovement: MutableState<Boolean> = mutableStateOf(false),
    val player1Goal: MutableState<Boolean> = mutableStateOf(false),
    val player2Goal: MutableState<Boolean> = mutableStateOf(false),
    val endGame: MutableState<Boolean> = mutableStateOf(false),
    val player1GoalCount: MutableState<Int> = mutableStateOf(0),
    val player2GoalCount: MutableState<Int> = mutableStateOf(0),
    var playerTwoOffsetX: Float = 461f,
    var playerTwoOffsetY: Float = 100f,
    var ballMovementYAxis: Float = 958f,
    var ballMovementXAxis: Float = 461f,
    val menuState: MutableState<Boolean> = mutableStateOf(false)
)

enum class GameTypeState {
    INITIAL,
    PLAYER_VS_CPU
}

@Composable
fun playerVsCpuState(gameState: GameState) : GameState {

    gameState.ballMovementYAxis = animateFloatAsState(
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
    ).value

    gameState.ballMovementXAxis = animateFloatAsState(
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

        ).value

    gameState.playerTwoOffsetX = when {
        gameState.menuState.value -> {
            gameState.ballMovementXAxis
        }
        else -> gameState.playerTwoStartOffsetX.value
    }.let {
        animateFloatAsState(
            targetValue = it,
            animationSpec = tween(1500, easing = LinearEasing)
        ).value
    }

    gameState.playerTwoOffsetY = animateFloatAsState(
        targetValue = when {
            gameState.downCollisionMovement.value -> {
                gameState.ballStartOffsetY.value // center line
            }
            else -> {
                gameState.playerTwoStartOffsetY.value
            }
        },
        animationSpec = tween(1500, easing = LinearEasing)
    ).value

    return gameState

}




