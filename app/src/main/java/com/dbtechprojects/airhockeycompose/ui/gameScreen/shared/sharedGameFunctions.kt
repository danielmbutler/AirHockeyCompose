package com.dbtechprojects.airhockeycompose.ui.gameScreen.shared

object sharedGameFunctions {

    fun determineWinner(player1Count: Int, player2Count: Int) : String{
        return if (player1Count > player2Count) "Player 1 Wins !"
        else "Player 2 Wins !"
    }
}