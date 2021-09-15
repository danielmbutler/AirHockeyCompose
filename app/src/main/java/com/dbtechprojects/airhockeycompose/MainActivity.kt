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
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dbtechprojects.airhockeycompose.ui.playerVCPU.*
import com.dbtechprojects.airhockeycompose.ui.shared.GameState
import com.dbtechprojects.airhockeycompose.ui.shared.GameTypeState
import com.dbtechprojects.airhockeycompose.ui.playerVCPU.playerVsCpuState
import com.dbtechprojects.airhockeycompose.ui.twoPlayerLocal.twoPlayerLocalState
import com.dbtechprojects.airhockeycompose.ui.twoPlayerLocal.TwoPlayerGameBoard
import com.dbtechprojects.airhockeycompose.ui.theme.AirHockeyComposeTheme

class MainActivity : ComponentActivity() {

    private lateinit var gameState: MutableState<GameState>
    private lateinit var gameTypeState: MutableState<GameTypeState>

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AirHockeyComposeTheme {
                // A surface container using the 'background' color from the theme

                gameTypeState = remember {
                    mutableStateOf(GameTypeState.INITIAL)
                }

                when (gameTypeState.value) {
                    GameTypeState.INITIAL -> {
                        gameState = remember { mutableStateOf(GameState()) }
                    }
                    GameTypeState.PLAYER_VS_CPU -> {
                        gameState.value = playerVsCpuState(gameState = gameState.value)
                    }
                    GameTypeState.TWO_PLAYER_LOCAL -> {
                        gameState.value = twoPlayerLocalState(gameState = gameState.value)
                    }
                }

                MainScreen(
                    {
                        gameTypeState.value = GameTypeState.PLAYER_VS_CPU
                    },
                    {
                        gameTypeState.value = GameTypeState.TWO_PLAYER_LOCAL
                    },
                    gameState,
                    gameTypeState
                )
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable
fun MainScreen(
    playerVsCpu: () -> Unit,
    twoPlayerLocal: () -> Unit,
    gameState: MutableState<GameState>,
    gameTypeState: MutableState<GameTypeState>
) {


    Surface(
        color = MaterialTheme.colors.background, modifier = Modifier
            .fillMaxSize()
    ) {

        Column(Modifier.padding(10.dp)) {

            when (gameTypeState.value) {
                GameTypeState.INITIAL -> {
                    GameBoard(playerVsCpu, gameState.value, twoPlayerLocal)
                }
                GameTypeState.PLAYER_VS_CPU -> {
                    GameBoard(playerVsCpu, gameState.value, twoPlayerLocal)
                }
                GameTypeState.TWO_PLAYER_LOCAL -> {
                    TwoPlayerGameBoard(playerVsCpu, gameState.value, twoPlayerLocal)
                }
            }
        }

    }
}