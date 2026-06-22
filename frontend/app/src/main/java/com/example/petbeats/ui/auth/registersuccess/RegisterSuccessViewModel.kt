package com.example.petbeats.ui.auth.registersuccess

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class RegisterSuccessViewModel(
    private val repository: AuthRepository
): ViewModel() {
    private var _event = MutableSharedFlow<RegisterSuccessEvent>()
    val event = _event.asSharedFlow()

    fun loginClick() {
        viewModelScope.launch {
//            val result = repository.loginUser()

//            if (result) {
//                _event.emit(RegisterSuccessEvent.NavigationLogin)
//            }
        }
    }
}