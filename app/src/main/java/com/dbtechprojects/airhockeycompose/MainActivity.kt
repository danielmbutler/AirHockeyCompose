package com.dbtechprojects.airhockeycompose

import android.os.Bundle
import android.util.Log
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
import com.dbtechprojects.airhockeycompose.ui.gameScreen.playerVCPU.GameTypeState
import com.dbtechprojects.airhockeycompose.ui.gameScreen.playerVCPU.playerVsCpuState
import com.dbtechprojects.airhockeycompose.ui.theme.AirHockeyComposeTheme

class MainActivity : ComponentActivity() {

    private lateinit var gameState: MutableState<GameState>
    private lateinit var gameTypeState: MutableState<GameTypeState>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AirHockeyComposeTheme {
                // A surface container using the 'background' color from the theme

                gameTypeState = remember {
                    mutableStateOf(GameTypeState.INITIAL)
                }

                if (gameTypeState.value == GameTypeState.INITIAL){
                    gameState = remember { mutableStateOf(GameState())}
                } else if (gameTypeState.value == GameTypeState.PLAYER_VS_CPU){
                    gameState.value = playerVsCpuState(gameState = gameState.value)
                }
                Log.d("MAIN", gameTypeState.toString())

                MainScreen({gameTypeState.value = GameTypeState.PLAYER_VS_CPU}, gameState)
            }
        }
    }
}

@Composable
fun MainScreen(playerVsCpu: () -> Unit, gameState: MutableState<GameState>) {


    Surface(
        color = MaterialTheme.colors.background, modifier = Modifier
            .fillMaxSize()
    ) {

        Column(Modifier.padding(10.dp)) {
            GameBoard(playerVsCpu,gameState.value)

        }

    }
}