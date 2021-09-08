package com.dbtechprojects.airhockeycompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dbtechprojects.airhockeycompose.ui.gameScreen.GameBorder
import com.dbtechprojects.airhockeycompose.ui.gameScreen.GameTitle
import com.dbtechprojects.airhockeycompose.ui.theme.AirHockeyComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AirHockeyComposeTheme {
                // A surface container using the 'background' color from the theme
                MainScreen()
            }
        }
    }
}

@Preview
@Composable
fun MainScreen() {
    Surface(
        color = MaterialTheme.colors.background, modifier = Modifier
            .fillMaxSize()
    ) {
        Column(Modifier.padding(10.dp)) {
            GameTitle(text = "Air Hockey Compose")
            GameBorder()
        }
    }
}