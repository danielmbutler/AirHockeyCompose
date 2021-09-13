package com.dbtechprojects.airhockeycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dbtechprojects.airhockeycompose.ui.gameScreen.playerVCPU.GameBoard
import com.dbtechprojects.airhockeycompose.ui.gameScreen.playerVCPU.GameState
import com.dbtechprojects.airhockeycompose.ui.theme.AirHockeyComposeTheme

class MainActivity : ComponentActivity() {

    private val menuState = mutableStateOf(false)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AirHockeyComposeTheme {
                // A surface container using the 'background' color from the theme
                val gameState = GameState(
                    playerOneOffsetX = remember { mutableStateOf(461f) },
                    playerOneStartOffsetY = remember { mutableStateOf(1780f) },
                    playerOneStartOffsetX = remember { mutableStateOf(461f) },
                    playerOneOffsetY = remember { mutableStateOf(1780f) },
                    playerTwoStartOffsetX = remember { mutableStateOf(461f) },
                    playerTwoStartOffsetY = remember { mutableStateOf(100f) },
                    ballStartOffsetX = remember { mutableStateOf(461f) },
                    ballStartOffsetY = remember { mutableStateOf(958f) },
                    upCollisionMovement = remember { mutableStateOf(false) },
                    downCollisionMovement = remember { mutableStateOf(false) },
                    goalCollisionMovement = remember { mutableStateOf(false) },
                    rightCollisionMovement = remember { mutableStateOf(false) },
                    leftCollisionMovement = remember { mutableStateOf(false) },
                    player1Goal = remember { mutableStateOf(false) },
                    player2Goal = remember {
                        mutableStateOf(false)
                    },
                    player1GoalCount = remember { mutableStateOf(0) },
                    player2GoalCount = remember { mutableStateOf(0) },
                    endGame = remember { mutableStateOf(false) }


                )
                MainScreen(menuState, gameState)
            }
        }
    }
}

@Composable
fun MainScreen(menuState: MutableState<Boolean>, gameState: GameState) {
    Surface(
        color = MaterialTheme.colors.background, modifier = Modifier
            .fillMaxSize()
    ) {

        Column(Modifier.padding(10.dp)) {
            GameBoard(menuState, gameState)

        }

    }
}