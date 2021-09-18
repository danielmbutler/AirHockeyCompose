package com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline


import android.util.Log
import androidx.compose.material.contentColorFor
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbtechprojects.airhockeycompose.di.DispatcherProvider
import com.dbtechprojects.airhockeycompose.network.SocketHandler
import com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline.models.Mappers
import com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline.state.ConnectionState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject


class GameEventViewModel(private val dispatcherProvider: DispatcherProvider) : ViewModel() {

    private var _gameEvents = MutableSharedFlow<String>(0)
    val gameEvents : MutableSharedFlow<String>
        get() = _gameEvents

    var name = ""

    private var _connectionState = MutableSharedFlow<ConnectionState>()
    val connectionState : MutableSharedFlow<ConnectionState>
        get() = _connectionState

    private val gameState = ConnectionState()

    // called from socket listener
    fun receiveGameEvent(text: String) {
        viewModelScope.launch(dispatcherProvider.io) {

            _gameEvents.emit(text)
            if (text.startsWith("connection confirmed : ")){
                if (gameState.playerOne.name.isEmpty()){
                    gameState.playerOne =  Mappers.jsonToPlayerObject(text.substringAfter("connection confirmed : "))
                    _connectionState.emit(gameState)
                    Log.d("viewmodel", "player found: $connectionState.playerOne")
                }
            }
            if(text.startsWith("paired")){
                gameState.playerList = Mappers.jsonToPlayerObjectList(text.substringAfter("paired : "))
                Log.d("viewmodel", "players found: ${gameState.playerList}")
                gameState.playerList.forEach {
                    if (it.playerID != gameState.playerOne.playerID){
                        gameState.playerTwo = it
                    }
                }
                gameState.gameFound = true
                _connectionState.emit(gameState)
                Log.d("viewmodel", "gameState: $connectionState.playerOne")
            }
        }
    }

    fun sendGameEvent(text: String){
        val jsonObject = JSONObject()
        try {
            jsonObject.put("name", text)
            SocketHandler.getSocket().send(jsonObject.toString())
        } catch (e: JSONException) {
            e.printStackTrace()

        }
    }

    fun onConnectionError(message: String?) {
        viewModelScope.launch(dispatcherProvider.io) {
            _gameEvents.emit(message ?: "error connecting")
        }
    }
}