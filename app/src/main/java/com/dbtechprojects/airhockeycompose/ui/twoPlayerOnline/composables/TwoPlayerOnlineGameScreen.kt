package com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dbtechprojects.airhockeycompose.ui.playerVCPU.GameBoard
import com.dbtechprojects.airhockeycompose.ui.shared.GameState
import com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline.composables.TwoPlayerOnlineGameBoard
import com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline.state.ConnectionState


@Composable
fun TwoPlayerOnlineGameMenu(
    gameState: MutableState<GameState>,
    viewModel: GameEventViewModel,
    connectAction: () -> Unit
) {
    var playerName by remember { mutableStateOf("") }
    var progress by remember { mutableStateOf(false) }
    val connectionStatus by viewModel.connectionState.collectAsState(initial = ConnectionState())
    val messages: String by viewModel.gameEvents.collectAsState(initial = "")
    Box(modifier = Modifier.fillMaxSize()) {
        TwoPlayerOnlineGameBoard(gameState = gameState.value, connectionState = connectionStatus)
        if (!connectionStatus.gameFound) {
            Row(
                Modifier
                    .fillMaxSize()
                    .background(
                        color = Color(0f, 0f, 0f, 0.6f),
                        shape = RectangleShape
                    ),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.Top,

                ) {
                Column {

                    Text(
                        text = "Provide name to connect",
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(20.dp),
                        fontSize = 32.sp, fontWeight = FontWeight.Bold
                    )

                    TextField(
                        value = playerName,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        onValueChange = { playerName = it },
                        colors = TextFieldDefaults.textFieldColors(
                            textColor = Color.White,
                            unfocusedLabelColor = Color.White
                        ),
                        label = { Text("name") }
                    )

                    Button(modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .padding(6.dp), onClick = {
                        connectAction.invoke()
                        viewModel.sendGameEvent(playerName)
                        progress = true
                    }) {
                        Text(text = "Find Game")

                    }

                    Text(text = "status: $messages")
                    if (progress) {
                        CircularProgressIndicator(
                            modifier = Modifier
                                .padding(6.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                    }
                }
            }
        }


    }

}

