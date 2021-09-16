package com.dbtechprojects.airhockeycompose.network

import org.json.JSONObject

interface SocketListener{
    fun emit(event:String, data: JSONObject)
    fun receive(args: Array<Any>)

}