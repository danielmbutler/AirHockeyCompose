package com.dbtechprojects.airhockeycompose.ui.shared

import androidx.compose.runtime.MutableState


object SharedGameFunctions {

    fun determineWinner(player1Count: Int, player2Count: Int) : String{
        return if (player1Count > player2Count) "Player 1 Wins !"
        else "Player 2 Wins !"
    }

    fun getMovementConditions(gameState: GameState) : List<MutableState<Boolean>>{
        return listOf(
            gameState.leftCollisionMovement,
            gameState.rightCollisionMovement,
            gameState.downCollisionMovement,
            gameState.upCollisionMovement,
            gameState.borderRightCollisionUpMovement,
            gameState.borderRightCollisionDownMovement,
            gameState.borderLeftCollisionDownMovement,
            gameState.borderLeftCollisionUpMovement,
            gameState.goalCollisionMovement,
            gameState.rightCollisionPlayerTwoMovement,
            gameState.leftCollisionPlayerTwoMovement,
        )
    }
    fun setMovementConditionsToFalse(list: List<MutableState<Boolean>>){
        list.forEach { it.value = false }
    }
}