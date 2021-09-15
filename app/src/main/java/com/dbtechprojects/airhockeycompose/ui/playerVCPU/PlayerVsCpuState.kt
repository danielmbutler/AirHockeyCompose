package com.dbtechprojects.airhockeycompose.ui.playerVCPU

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import com.dbtechprojects.airhockeycompose.ui.shared.GameState

@Composable
fun playerVsCpuState(gameState: GameState): GameState {

    return gameState.copy(
        playerTwoOffsetX = when {
            gameState.menuState.value -> {
                gameState.ballMovementXAxis
            }
            else -> gameState.playerTwoStartOffsetX.value
        }.let {
            animateFloatAsState(
                targetValue = it,
                animationSpec = tween(1500, easing = LinearEasing)
            ).value
        },
        playerTwoOffsetY = animateFloatAsState(
            targetValue = when {
                gameState.downCollisionMovement.value -> {
                    gameState.ballStartOffsetY.value // center line
                }
                else -> {
                    gameState.playerTwoStartOffsetY.value
                }
            },
            animationSpec = tween(1500, easing = LinearEasing)
        ).value,
        ballMovementYAxis = animateFloatAsState(
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
        ).value,
        ballMovementXAxis = animateFloatAsState(
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
    )

}