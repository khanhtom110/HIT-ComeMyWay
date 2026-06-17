package com.example.petbeats.ui.auth.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class HomeViewModel: ViewModel() {
    private val _event = MutableSharedFlow<HomeEvent>()
    val event = _event.asSharedFlow()

    fun loginClick() {
        viewModelScope.launch {
            _event.emit(HomeEvent.NavigationLogin)
        }
    }

    fun registerClick() {
        viewModelScope.launch {
            _event.emit(HomeEvent.NavigationRegister)
        }
    }
}