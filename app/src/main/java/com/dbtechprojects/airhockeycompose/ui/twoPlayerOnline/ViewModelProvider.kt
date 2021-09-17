package com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dbtechprojects.airhockeycompose.di.DispatcherProvider
import kotlinx.coroutines.InternalCoroutinesApi

class ViewModelFactory(

    private val dispatcherProvider: DispatcherProvider
) : ViewModelProvider.NewInstanceFactory() {

    @InternalCoroutinesApi
    override fun <T : ViewModel?> create(modelClass: Class<T>): T =
        GameEventViewModel(dispatcherProvider) as T


}