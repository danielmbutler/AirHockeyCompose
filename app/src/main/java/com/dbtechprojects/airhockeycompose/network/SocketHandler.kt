package com.dbtechprojects.airhockeycompose.network

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject
import java.net.URISyntaxException


//https://medium.com/@thushenarriyam/socket-io-connection-on-android-kotlin-to-node-js-server-71b218c160c9

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
// "http://10.0.2.2:3000" is the network your Android emulator must use to join the localhost network on your computer
// "http://localhost:3000/" will not work
// If you want to use your physical phone you could use the your ip address plus :3000
// This will allow your Android Emulator and physical device at your home to connect to the server
    Log.d("sockHandler", "setting Socket" )
            mSocket = IO.socket("http://192.168.1.156:3000")

        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {

        mSocket.connect()
        Log.d("sockHandler", "connection : ${mSocket.connected()}" )
        mSocket.emit("connection", JSONObject().put("test", "test"))
    }

    @Synchronized
    fun closeConnection() {
        if(this::mSocket.isInitialized) mSocket.disconnect()
    }
}