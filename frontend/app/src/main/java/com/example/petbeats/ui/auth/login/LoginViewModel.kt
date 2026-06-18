package com.example.petbeats.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel: ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<LoginEvent>()
    val event = _event.asSharedFlow()

    fun changeEye() {
        _state.value = _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)
    }

    fun forgotCLick() {
        viewModelScope.launch {
            _event.emit(LoginEvent.NavigationForgot)
        }
    }

    fun registerClick() {
        viewModelScope.launch {
            _event.emit(LoginEvent.NavigationRegister)
        }
    }

    fun onNameChange(name: String) {
        viewModelScope.launch {
            _state.value =_state.value.copy(name = name)
        }
    }

    fun onPasswordChange(password: String) {
        viewModelScope.launch {
            _state.value =_state.value.copy(password = password)
        }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            val name = _state.value.name.trim()
            val password = _state.value.password.trim()

            if (name.isEmpty() || password.isEmpty()) {
                _state.value = _state.value.copy(isName = true, isPassword = true, error = "Vui lòng nhập đầy đủ thông tin")
                return@launch
            }
            else if (password.length < 6) {
                _state.value = _state.value.copy(isName = false, isPassword = true, error = "Mật khẩu phải lớn hơn 6 số")
                return@launch
            }

            else {
                _state.value = _state.value.copy(isName = false, isPassword = false, error = "")
                _event.emit(LoginEvent.NavigationHome)
            }
        }
    }
}