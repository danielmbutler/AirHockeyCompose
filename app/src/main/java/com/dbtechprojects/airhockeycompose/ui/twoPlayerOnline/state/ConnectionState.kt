package com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline.state

import com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline.models.Player

data class ConnectionState(
    var gameFound: Boolean = false,
    var playerOne: Player = Player("", 0, ""),
    var playerTwo: Player = Player("", 0, ""),
    var playerList: List<Player> = listOf(playerOne, playerTwo)
)
