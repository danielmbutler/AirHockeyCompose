package com.dbtechprojects.airhockeycompose.ui.shared

import androidx.compose.runtime.*

// creates base game state within constructor
data class GameState(
    val playerOneStartOffsetX: MutableState<Float> = mutableStateOf(461f),
    val playerOneStartOffsetY: MutableState<Float> = mutableStateOf(1780f),
    val playerOneOffsetX: MutableState<Float> = mutableStateOf(461f),
    val playerOneOffsetY: MutableState<Float> = mutableStateOf(1780f),
    val playerTwoStartOffsetX: MutableState<Float> = mutableStateOf(461f),
    val playerTwoStartOffsetY: MutableState<Float> = mutableStateOf(100f),
    val ballStartOffsetX: MutableState<Float> = mutableStateOf(461f),
    val ballStartOffsetY: MutableState<Float> = mutableStateOf(958f),
    val upCollisionMovement: MutableState<Boolean> = mutableStateOf(false),
    val downCollisionMovement: MutableState<Boolean> = mutableStateOf(false),
    val leftCollisionMovement: MutableState<Boolean> = mutableStateOf(false),
    val rightCollisionMovement: MutableState<Boolean> = mutableStateOf(false),
    val goalCollisionMovement: MutableState<Boolean> = mutableStateOf(false),
    val leftCollisionPlayerTwoMovement: MutableState<Boolean> = mutableStateOf(false),
    val rightCollisionPlayerTwoMovement: MutableState<Boolean> = mutableStateOf(false),
    val borderLeftCollisionDownMovement: MutableState<Boolean> = mutableStateOf(false),
    val borderLeftCollisionUpMovement: MutableState<Boolean> = mutableStateOf(false),
    val borderRightCollisionDownMovement: MutableState<Boolean> = mutableStateOf(false),
    val borderRightCollisionUpMovement: MutableState<Boolean> = mutableStateOf(false),
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
    PLAYER_VS_CPU,
    TWO_PLAYER_LOCAL,
    TWO_PLAYER_ONLINE
}


