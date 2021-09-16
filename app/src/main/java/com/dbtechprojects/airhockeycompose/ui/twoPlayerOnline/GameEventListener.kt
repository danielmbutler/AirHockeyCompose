package com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline

import android.util.Log
import com.dbtechprojects.airhockeycompose.network.SocketHandler
import com.dbtechprojects.airhockeycompose.network.SocketListener
import org.json.JSONObject

object GameEventListener : SocketListener {
    override fun emit(event: String, data: JSONObject) {
        SocketHandler.getSocket().emit(event, "test")
        Log.d("sending" , event + SocketHandler.getSocket().id())
    }

    override fun receive(args: Array<Any>) {
        Log.d("received" , args.toString())
    }
}