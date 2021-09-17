package com.dbtechprojects.airhockeycompose.di

import androidx.lifecycle.ViewModelProvider
import com.dbtechprojects.airhockeycompose.ui.twoPlayerOnline.ViewModelFactory

// manual dependency Injection class
object Injection {


    fun provideViewModelFactory(): ViewModelProvider.Factory {
        return ViewModelFactory(dispatcherProvider = DispatcherImpl)
    }
}