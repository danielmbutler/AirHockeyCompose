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
import com.dbtechprojects.airhockeycompose.ui.gameScreen.playerVCPU.playerVsCpuState
import com.dbtechprojects.airhockeycompose.ui.theme.AirHockeyComposeTheme

class MainActivity : ComponentActivity() {

    private val menuState = mutableStateOf(false)



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AirHockeyComposeTheme {
                // A surface container using the 'background' color from the theme
                val gameState = this.playerVsCpuState()
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