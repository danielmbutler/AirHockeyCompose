package com.dbtechprojects.airhockeycompose.network

import android.util.Log
import com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline.GameEventViewModel

import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener


class SocketListener(private val gameEventViewModel: GameEventViewModel): WebSocketListener() {


    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        Log.d("SocketListener", "message received: $text")
        gameEventViewModel.receiveGameEvent(text)
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d("SocketListener" , response.toString())
        gameEventViewModel.receiveGameEvent("Connected searching for game ...")


    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        gameEventViewModel.onConnectionError(t.message)
        t.printStackTrace()
    }
}