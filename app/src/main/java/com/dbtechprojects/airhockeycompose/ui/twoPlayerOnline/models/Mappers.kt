package com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline.models


import com.google.gson.GsonBuilder

object Mappers {

    fun jsonToPlayerObjectList(string: String) : List<Player>{
        val gson = GsonBuilder().create()
        return gson.fromJson(string,Array<Player>::class.java).toList()
    }

    fun jsonToPlayerObject(string: String) : Player{
        val gson = GsonBuilder().create()
        return gson.fromJson(string,Player::class.java)
    }

}