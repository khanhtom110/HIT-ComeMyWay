package com.example.petbeats.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.data.repository.AuthRepository
import com.example.petbeats.ui.auth.login.request_response.LoginRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
): ViewModel() {
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
                _state.value = _state.value.copy(isName = true, isPassword = true, nameError = "Nhập tên", passwordError = "Nhập password")
                return@launch
            }
            else if (password.length < 7) {
                _state.value = _state.value.copy(isName = false, isPassword = true, nameError = "", passwordError = "Mật khẩu phải lớn hơn 7 số")
                return@launch
            }


            _event.emit(LoginEvent.NavigationHome)

            val request = LoginRequest(name, password)
            val result = repository.loginUser(request)

            if (result) {
                _state.value = _state.value.copy(isName = false, isPassword = false)
            }
            else {
                _state.value = _state.value.copy()
            }
        }
    }
}