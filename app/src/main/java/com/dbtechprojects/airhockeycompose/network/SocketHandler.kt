package com.dbtechprojects.airhockeycompose.network

import android.util.Log
import com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline.GameEventViewModel
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import java.net.URISyntaxException


//https://medium.com/@thushenarriyam/socket-io-connection-on-android-kotlin-to-node-js-server-71b218c160c9

object SocketHandler {

    lateinit var mSocket: WebSocket
    private var SERVER_PATH = "ws://192.168.1.156:3000"

    @Synchronized
    fun setSocket(viewModel: GameEventViewModel) {
        try {
    Log.d("socketHandler", "setting Socket" )
            val client = OkHttpClient()
            val request = Request.Builder().url(SERVER_PATH).build()
            mSocket = client.newWebSocket(request, SocketListener(viewModel))


        } catch (e: Exception) {
            when(e){
               is  URISyntaxException -> e.printStackTrace()

                else -> e.printStackTrace()
            }

        }
    }

    fun getSocket(): WebSocket {
        return mSocket
    }

    @Synchronized
    fun closeConnection() {
        if(this::mSocket.isInitialized) mSocket.close(1, "")
    }
}