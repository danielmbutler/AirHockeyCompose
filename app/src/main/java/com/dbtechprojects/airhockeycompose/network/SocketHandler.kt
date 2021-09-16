package com.dbtechprojects.airhockeycompose.network

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.engineio.client.EngineIOException
import org.json.JSONObject
import java.net.URI
import java.net.URISyntaxException


//https://medium.com/@thushenarriyam/socket-io-connection-on-android-kotlin-to-node-js-server-71b218c160c9

object SocketHandler {

    lateinit var mSocket: Socket

    @Synchronized
    fun setSocket() {
        try {
    Log.d("socketHandler", "setting Socket" )
            mSocket = IO.socket("http://192.168.1.156:3000")

            mSocket.on(Socket.EVENT_CONNECT_ERROR){
                Log.d("SocketHandler" ,"Connection Error : ${it}")
                it.forEach { item ->
                    val exception = item as EngineIOException
                    print(exception.message + " " + exception.code + " " + exception.cause)
                }
            }
            mSocket.on(Socket.EVENT_CONNECT_TIMEOUT){
                Log.d("SocketHandler", "TimeOut Error : $it")
            }
            establishConnection()

        } catch (e: Exception) {
            when(e){
               is  URISyntaxException -> e.printStackTrace()
               is  EngineIOException -> {
                   Log.d("EngineException", "${e.code}, ${e.transport}, ${e.message}")
                   e.printStackTrace()
               }
                else -> e.printStackTrace()
            }

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {

        mSocket.connect()
        Log.d("socketHandler", "connection : ${mSocket.connected()}" )
        mSocket.emit("connection")
    }

    @Synchronized
    fun closeConnection() {
        if(this::mSocket.isInitialized) mSocket.disconnect()
    }
}