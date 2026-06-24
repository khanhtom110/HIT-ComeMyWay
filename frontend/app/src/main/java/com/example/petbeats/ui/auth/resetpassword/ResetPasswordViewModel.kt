package com.example.petbeats.ui.auth.resetpassword

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.remote.model.RefreshTokenRequest
import com.example.petbeats.data.repository.AuthRepository
import com.example.petbeats.data.repository.ErrorTarget
import com.example.petbeats.ui.auth.login.LoginEvent
import com.example.petbeats.ui.auth.login.request_response.LoginRequest
import com.example.petbeats.ui.auth.resetpassword.request_response.ResetPasswordRequest
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

    fun onSuccessClick(token: String) {
        viewModelScope.launch {
            val password = _state.value.password.trim()
            val password1 = _state.value.password1.trim()


//            if (password.length < 7 || password1.length < 7) {
//                _state.value = _state.value.copy(isPassword = true, isPassword1 = true, passwordError = "Mật khẩu chưa đủ mạnh (cần chữ hoa, chữ thường, số và \nký tự đặc biệt).", passwordError1 = "Mật khẩu chưa đủ mạnh (cần chữ hoa, chữ thường, số và ký tự đặc biệt).")
//                return@launch
//            }



            val request = ResetPasswordRequest(token, password, password1)
            val result = repository.resetpasswordUser(request)


            when (result) {
                is DataResult.Success -> {
                    _state.value = _state.value.copy(isPassword = false, isPassword1 = false, passwordError = "", passwordError1 = "")

                    _event.emit(ResetPasswordEvent.NavigationSuccess)
                }

                is DataResult.Error -> {
                    _state.value = _state.value.copy(
                        isPassword = (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL),
                        isPassword1 = (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL),
                        passwordError = if (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL) result.message else "",
                        passwordError1 = if (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL) result.message else ""
                    )
                    return@launch
                }
            }
        }
    }
}