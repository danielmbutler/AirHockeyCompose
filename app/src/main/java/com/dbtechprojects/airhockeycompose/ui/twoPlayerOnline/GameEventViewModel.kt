package com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline


import android.widget.Toast
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

    // called from socket listener
    fun receiveGameEvent(text: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            _gameEvents.emit(text)
        }
    }

    fun sendGameEvent(text: String){
        val jsonObject = JSONObject()
        try {
            jsonObject.put("name", "test")
            jsonObject.put("message", text)
            SocketHandler.getSocket().send(jsonObject.toString())
        } catch (e: JSONException) {
            e.printStackTrace()

        }
    }
}