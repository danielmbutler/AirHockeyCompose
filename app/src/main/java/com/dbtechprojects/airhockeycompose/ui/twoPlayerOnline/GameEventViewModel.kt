package com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline


import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dbtechprojects.airhockeycompose.di.DispatcherProvider
import com.dbtechprojects.airhockeycompose.network.SocketHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject


class GameEventViewModel(private val dispatcherProvider: DispatcherProvider) : ViewModel() {

    private var _gameEvents = MutableStateFlow("")
    val gameEvents : StateFlow<String>
        get() = _gameEvents

    var gameFound = false

    // called from socket listener
    fun receiveGameEvent(text: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            _gameEvents.emit(text)
            if (text == "paired"){
                gameFound = true
            }
        }
    }

    fun sendGameEvent(text: String){
        val jsonObject = JSONObject()
        try {
            jsonObject.put("name", text)
            jsonObject.put("message", text)
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