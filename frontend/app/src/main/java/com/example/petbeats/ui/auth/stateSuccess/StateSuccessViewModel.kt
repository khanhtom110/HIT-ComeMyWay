package com.example.petbeats.ui.auth.stateSuccess

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class StateSuccessViewModel: ViewModel() {
    private val _event = MutableSharedFlow<StateSuccessEvent>()
    val event = _event.asSharedFlow()

    fun loginClick() {
        viewModelScope.launch {
            _event.emit(StateSuccessEvent.NavigationLogin)
        }
    }
}