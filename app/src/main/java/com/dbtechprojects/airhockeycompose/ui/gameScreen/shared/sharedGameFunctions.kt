package com.dbtechprojects.airhockeycompose.ui.gameScreen.shared

import android.util.Log
import android.view.MotionEvent
import androidx.compose.ui.geometry.Offset
import java.lang.IndexOutOfBoundsException

object sharedGameFunctions {

    fun determineWinner(player1Count: Int, player2Count: Int) : String{
        return if (player1Count > player2Count) "Player 1 Wins !"
        else "Player 2 Wins !"
    }

    fun getSecondPointerCoordinates(motionEvent: MotionEvent) : Offset{
        return if (motionEvent.pointerCount > 1){
            try {
                val (pointer2X: Float, pointer2Y: Float) = motionEvent.findPointerIndex(1).let { pointerIndex ->
                    // Get the pointer's current position
                    motionEvent.getX(pointerIndex) to motionEvent.getY(pointerIndex)
                }
                Log.d("pointer 2", "x: $pointer2X, y: $pointer2Y")
                (Offset(pointer2X, pointer2Y))

            }catch (e: IndexOutOfBoundsException){
                Offset(0f,0f)
            }

        } else Offset(0f,0f)
    }
}