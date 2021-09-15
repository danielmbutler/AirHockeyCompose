package com.dbtechprojects.airhockeycompose.ui.gameScreen.shared

import android.util.Log
import android.util.Range
import android.view.MotionEvent
import androidx.compose.runtime.MutableState
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

    fun getPlayerOnePosition(
        motionEvent: MotionEvent,
        playerOneOffsetX : MutableState<Float>,
        playerOneOffsetY: MutableState<Float>,
        endGame: MutableState<Boolean>
    )
    : Offset? {

        var positionX = 0f
        var positionY = 0f
        if (Range
                .create(
                    playerOneOffsetX.value - 100f,
                    playerOneOffsetX.value + 100f
                )
                .contains(motionEvent.x) && Range
                .create(
                    playerOneOffsetY.value - 100f,
                    playerOneOffsetY.value + 100f
                )
                .contains(motionEvent.y) && !endGame.value
        ) {


            if (playerOneOffsetX.value < 805f && playerOneOffsetX.value > 160f) {
                //player is within boundary so update location
                positionX = motionEvent.x
            } else if (playerOneOffsetX.value > 805f && motionEvent.x < playerOneOffsetX.value) {
                // player is almost at the right most edge of boundary so only accept drags to the left
                positionX = motionEvent.x
            } else if (playerOneOffsetX.value < 160f && motionEvent.x > playerOneOffsetX.value) {
                // player is almost at the left most edge of boundary so only accept drags to the right
                positionX = motionEvent.x
            }

            // handle Vertical dragging
            if (playerOneOffsetY.value > 1050f && playerOneOffsetY.value < 1790f) {
                positionY = motionEvent.y
            } else if (playerOneOffsetY.value > 1790f && motionEvent.y < playerOneOffsetY.value) {
                positionY = motionEvent.y
            } else if (playerOneOffsetY.value < 1050f && motionEvent.y > playerOneOffsetY.value) {
                positionY = motionEvent.y
            }

            return (Offset(positionX, positionY))
            // if second pointer is used for player one (player one is not touched first
        } else if (motionEvent.pointerCount > 1) {
            val secondPointerX = getSecondPointerCoordinates(motionEvent).x
            val secondPointerY = getSecondPointerCoordinates(motionEvent).y
            if (Range
                    .create(
                        playerOneOffsetX.value - 100f,
                        playerOneOffsetX.value + 100f
                    )
                    .contains(secondPointerX) && Range
                    .create(
                        playerOneOffsetY.value - 100f,
                        playerOneOffsetY.value + 100f
                    )
                    .contains(secondPointerY) && !endGame.value
            ) {


                if (playerOneOffsetX.value < 805f && playerOneOffsetX.value > 160f) {
                    //player is within boundary so update location
                    positionX = secondPointerX
                } else if (playerOneOffsetX.value > 805f && secondPointerX < playerOneOffsetX.value) {
                    // player is almost at the right most edge of boundary so only accept drags to the left
                    positionX = secondPointerX
                } else if (playerOneOffsetX.value < 160f && secondPointerX > playerOneOffsetX.value) {
                    // player is almost at the left most edge of boundary so only accept drags to the right
                    positionX = secondPointerX
                }

                // handle Vertical dragging
                if (playerOneOffsetY.value > 1050f && playerOneOffsetY.value < 1790f) {
                    positionY = secondPointerY
                } else if (playerOneOffsetY.value > 1790f && secondPointerY < playerOneOffsetY.value) {
                    positionY = secondPointerY
                } else if (playerOneOffsetY.value < 1050f && secondPointerY > playerOneOffsetY.value) {
                    positionY = secondPointerY
                }
                return (Offset(positionX, positionY))
            } else return null


        } else return null
    }
}