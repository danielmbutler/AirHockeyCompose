package com.dbtechprojects.airhockeycompose.network

import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException


//https://medium.com/@thushenarriyam/socket-io-connection-on-android-kotlin-to-node-js-server-71b218c160c9

object SocketHandler {

    lateinit var mSocket: Socket
    lateinit var mListener: SocketListener

    @Synchronized
    fun setSocket(listener: SocketListener) {
        try {
// "http://10.0.2.2:3000" is the network your Android emulator must use to join the localhost network on your computer
// "http://localhost:3000/" will not work
// If you want to use your physical phone you could use the your ip address plus :3000
// This will allow your Android Emulator and physical device at your home to connect to the server
            mSocket = IO.socket("http://10.0.2.2:3000")
            mSocket.on("connection"){
                listener.receive(it)
            }
        } catch (e: URISyntaxException) {

        }
    }

    @Synchronized
    fun getSocket(): Socket {
        return mSocket
    }

    @Synchronized
    fun establishConnection() {
        mSocket.connect()
    }

    @Synchronized
    fun closeConnection() {
        mSocket.disconnect()
    }
}