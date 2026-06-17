package com.example.petbeats.ui.auth.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel: ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<RegisterEvent>()
    val event = _event.asSharedFlow()

    fun changeEye() {
        _state.value = _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)
    }

    fun changeEye1() {
        _state.value = _state.value.copy(isPasswordVisible1 = !_state.value.isPasswordVisible1)
    }

    fun loginClick() {
        viewModelScope.launch {
            _event.emit(RegisterEvent.NavigationLogin)
        }
    }

    fun onNameChange(name: String) {
        _state.value = _state.value.copy(name = name)
    }

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun onPasswordChange1(password1: String) {
        _state.value = _state.value.copy(password1 = password1)
    }

    fun onLoginClick() {
        viewModelScope.launch {
            val name = _state.value.name.trim()
            val email = _state.value.email.trim()
            val password = _state.value.password.trim()
            val password1 = _state.value.password1.trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty() || password1.isEmpty()) {
                _state.value = _state.value.copy(isName = true, isEmail = true, isPassword = true, isPassword1 = true, error = "Vui lòng nhập đầy đủ thông tin")
                return@launch
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _state.value = _state.value.copy(isName = false, isEmail = true, isPassword = false, isPassword1 = false, error = "Nhập không đúng email. Nhập lại!")
                return@launch
            }
            else if (password.length < 6 || password1.length < 6) {
                _state.value = _state.value.copy(isName = false, isEmail = false, isPassword = true, isPassword1 = true, error = "Mật khẩu phải lớn hơn 6 số")
                return@launch
            }
            else if (password != password1) {
                _state.value = _state.value.copy(isName = false, isEmail = false, isPassword = true, isPassword1 = true, error = "Hai mật khẩu không trùng khớp")
                return@launch
            }
            else {
                _state.value = _state.value.copy(isName = false, isEmail = false, isPassword = false, isPassword1 = false, error = "")
                _event.emit(RegisterEvent.NavigationLogin)
            }
        }
    }
}