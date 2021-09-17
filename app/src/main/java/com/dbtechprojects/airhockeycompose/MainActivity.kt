package com.dbtechprojects.airhockeycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import com.dbtechprojects.airhockeycompose.di.Injection
import com.dbtechprojects.airhockeycompose.network.SocketHandler
import com.dbtechprojects.airhockeycompose.network.SocketHandler.setSocket
import com.dbtechprojects.airhockeycompose.ui.playerVCPU.*
import com.dbtechprojects.airhockeycompose.ui.shared.GameState
import com.dbtechprojects.airhockeycompose.ui.shared.GameTypeState
import com.dbtechprojects.airhockeycompose.ui.playerVCPU.playerVsCpuState
import com.dbtechprojects.airhockeycompose.ui.twoPlayerLocal.twoPlayerLocalState
import com.dbtechprojects.airhockeycompose.ui.twoPlayerLocal.TwoPlayerGameBoard
import com.dbtechprojects.airhockeycompose.ui.theme.AirHockeyComposeTheme
import com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline.GameEventViewModel
import android.content.Intent
import android.util.Log
import com.google.android.gms.security.ProviderInstaller


class MainActivity : ComponentActivity() {

    private lateinit var gameState: MutableState<GameState>
    private lateinit var gameTypeState: MutableState<GameTypeState>
    private lateinit var gameEventViewModel: GameEventViewModel

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        gameEventViewModel = ViewModelProvider(
            this,
            Injection.provideViewModelFactory()
        ).get(GameEventViewModel::class.java)

        ProviderInstaller.installIfNeededAsync(this, object :
            ProviderInstaller.ProviderInstallListener {
            override fun onProviderInstalled() {}
            override fun onProviderInstallFailed(i: Int, intent: Intent?) {
                Log.i("main", "Provider install failed ($i) : SSL Problems may occurs")
            }
        })
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
                    GameTypeState.TWO_PLAYER_ONLINE -> {

                        setSocket(gameEventViewModel)
                    }
                }

                MainScreen(
                    playerVsCpu =
                    {
                        gameTypeState.value = GameTypeState.PLAYER_VS_CPU
                    },
                    twoPlayerLocal =
                    {
                        gameTypeState.value = GameTypeState.TWO_PLAYER_LOCAL
                    },
                    twoPlayerOnline =
                    {
                        gameTypeState.value = GameTypeState.TWO_PLAYER_ONLINE
                    },
                    gameState = gameState,
                    gameTypeState = gameTypeState,
                    viewModel = gameEventViewModel
                )
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        SocketHandler.closeConnection()
    }
}

@ExperimentalComposeUiApi
@Composable
fun MainScreen(
    playerVsCpu: () -> Unit,
    twoPlayerLocal: () -> Unit,
    twoPlayerOnline: () -> Unit,
    gameState: MutableState<GameState>,
    viewModel: GameEventViewModel?,
    gameTypeState: MutableState<GameTypeState>
) {


    Surface(
        color = MaterialTheme.colors.background, modifier = Modifier
            .fillMaxSize()
    ) {

        Box(Modifier.padding(10.dp)) {

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
                GameTypeState.TWO_PLAYER_ONLINE -> {
                    viewModel?.let {

                        var messageToSend = ""
                        val messages: String by it.gameEvents.collectAsState()
                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .padding(6.dp)) {
                            TextField(
                                value = "type message to send",
                                onValueChange = { messageToSend = it })

                            Button(onClick = { viewModel.sendGameEvent(messageToSend) }) {
                                Text(text = "test")

                            }

                            Text(text = "received : $messages")

                        }

                    }

                }
            }

            if (!gameState.value.menuState.value) {
                GameMenu(
                    playerVsCpuState = playerVsCpu,
                    twoPlayerLocal = twoPlayerLocal,
                    twoPlayerOnline = twoPlayerOnline,
                    onGameButtonClick = { gameState.value.menuState.value = true })
            }
        }

    }
}