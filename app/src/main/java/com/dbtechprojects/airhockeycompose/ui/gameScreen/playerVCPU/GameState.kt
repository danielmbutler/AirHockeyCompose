package com.dbtechprojects.airhockeycompose.ui.gameScreen.playerVCPU

import androidx.compose.runtime.*

data class GameState(
    val playerOneStartOffsetX: MutableState<Float>,
    val playerOneStartOffsetY: MutableState<Float>,
    val playerOneOffsetX: MutableState<Float>,
    val playerOneOffsetY: MutableState<Float>,
    val playerTwoStartOffsetX: MutableState<Float>,
    val playerTwoStartOffsetY: MutableState<Float>,
    val ballStartOffsetX : MutableState<Float>,
    val ballStartOffsetY : MutableState<Float>,
    val upCollisionMovement: MutableState<Boolean>,
    val downCollisionMovement: MutableState<Boolean>,
    val leftCollisionMovement: MutableState<Boolean>,
    val rightCollisionMovement: MutableState<Boolean>,
    val goalCollisionMovement: MutableState<Boolean>,
    val player1Goal: MutableState<Boolean>,
    val player2Goal: MutableState<Boolean>,
    val endGame: MutableState<Boolean>,
    val player1GoalCount: MutableState<Int>,
    val player2GoalCount: MutableState<Int>,
)

//// defining collision types
//var upCollisionMovement by remember {
//    mutableStateOf(false)
//}
//var downCollisionMovement by remember {
//    mutableStateOf(false)
//}
//var leftCollisionMovement by remember {
//    mutableStateOf(false)
//}
//var rightCollisionMovement by remember {
//    mutableStateOf(false)
//}
//var goalCollisionMovement by remember {
//    mutableStateOf(false)
//}
//var player1GoalCount by remember {
//    mutableStateOf(0)
//}
//var player2GoalCount by remember {
//    mutableStateOf(0)
//}
//var player1Goal by remember {
//    mutableStateOf(false)
//}
//var player2Goal by remember {
//    mutableStateOf(false)
//}
//var endGame by remember {
//    mutableStateOf(false)
//}



//val playerOneStartOffsetX by remember { mutableStateOf(461f) }
//val playerOneStartOffsetY by remember { mutableStateOf(1780f) }
//var playerOneOffsetX by remember { mutableStateOf(461f) }
//var playerOneOffsetY by remember { mutableStateOf(1780f) }
//val playerTwoStartOffsetX by remember { mutableStateOf(461f) }
//val playerTwoStartOffsetY by remember { mutableStateOf(100f) }
//val ballStartOffsetX by remember { mutableStateOf(461f) }
//val ballStartOffsetY by remember { mutableStateOf(958f) }
//