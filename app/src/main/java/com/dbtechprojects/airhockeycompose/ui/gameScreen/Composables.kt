package com.dbtechprojects.airhockeycompose.ui.gameScreen


import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun GameTitle(text: String) {
    Row(
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .padding(6.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            textAlign = TextAlign.Center,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
        )
    }
}

@Composable
fun GameBorder() {
    Box(
        Modifier
            .border(6.dp, color = Color.White, shape = RectangleShape)
            .background(Color.Gray)
            .fillMaxSize()
    )
    {

       Column() {
           Canvas(modifier = Modifier
               .fillMaxSize()
               .padding(20.dp)){
               val height = this.size.height
               val width = this.size.width

                drawBorder(height, width)
           }
       }
        GameMenu()
    }
}

@Composable
fun GameMenu () {
    Box(modifier = Modifier
        .fillMaxSize()
        .padding(20.dp)
        .background(color = Color(0f, 0f, 0f, 0.6f), shape = RectangleShape,) ){

    }
}
