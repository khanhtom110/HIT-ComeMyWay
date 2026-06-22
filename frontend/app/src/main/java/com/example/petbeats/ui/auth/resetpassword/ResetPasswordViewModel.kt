package com.example.petbeats.ui.auth.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ResetPasswordViewModel(
    private val repository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(ResetPasswordState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ResetPasswordEvent>()
    val event = _event.asSharedFlow()

    fun changePassword() {
        _state.value = _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)
    }

    fun changePassword1() {
        _state.value = _state.value.copy(isPasswordVisible1 = !_state.value.isPasswordVisible1)
    }

    fun onLoginClick() {
        viewModelScope.launch {
            _event.emit(ResetPasswordEvent.NavigationLogin)
        }
    }

    fun otpClick() {
        viewModelScope.launch {
            _event.emit(ResetPasswordEvent.NavigationOTP)
        }
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun onPasswordChange1(password1: String) {
        _state.value = _state.value.copy(password1 = password1)
    }

    fun onSuccessClick(email: String, otp: String) {
        viewModelScope.launch {
            val password = _state.value.password.trim()
            val password1 = _state.value.password1.trim()

            if (password.isEmpty() || password1.isEmpty()) {
                _state.value = _state.value.copy(isPassword = true, isPassword1 = true, passwordError = "Vui lòng nhập đầy đủ thông tin", passwordError1 = "Vui lòng nhập đầy đủ thông tin")
                return@launch
            }
            else if (password.length < 7 || password1.length < 7) {
                _state.value = _state.value.copy(isPassword = true, isPassword1 = true, passwordError = "Mật khẩu phải lớn hơn 7 số", passwordError1 = "Mật khẩu phải lớn hơn 7 số")
                return@launch
            }
            else if (password != password1) {
                _state.value = _state.value.copy(isPassword = true, isPassword1 = true)
                return@launch
            }
            else {
                _state.value = _state.value.copy(isPassword = false, isPassword1 = false)
                _event.emit(ResetPasswordEvent.NavigationSuccess)
            }
        }
    }
}