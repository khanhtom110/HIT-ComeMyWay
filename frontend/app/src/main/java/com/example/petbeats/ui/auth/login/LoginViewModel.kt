package com.example.petbeats.ui.auth.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.repository.AuthRepository
import com.example.petbeats.data.repository.ErrorTarget
import com.example.petbeats.data.remote.model.calendar.auth.request.LoginRequest
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
            _state.value =_state.value.copy(name = name, isName = false)
        }
    }

    fun onPasswordChange(password: String) {
        viewModelScope.launch {
            _state.value =_state.value.copy(password = password, isPassword = false)
        }
    }

    fun onLoginClick() {
        viewModelScope.launch {
            val name = _state.value.name.trim()
            val password = _state.value.password.trim()

//            if (password.length < 7) {
//                _state.value = _state.value.copy(isName = false,isPassword = true, nameError = "", passwordError = "Mật khẩu chưa đủ mạnh (cần chữ hoa, chữ thường, số và \nký tự đặc biệt).")
//                return@launch
//            }


            val request = LoginRequest(name, password)
            val result = repository.loginUser(request)

            when (result) {
                is DataResult.Success -> {
                    _state.value = _state.value.copy(isName = false, isPassword = false, nameError = "", passwordError = "")

                    // Lấy Token từ result.data để lưu vào DataStore/SharedPreferences
                    val accessToken = result.data.accessToken ?: ""
                    val refreshToken = result.data.refreshToken ?: ""

                    _event.emit(LoginEvent.NavigationHome(accessToken, refreshToken))
                }

                is DataResult.Error -> {
                    _state.value = _state.value.copy(
                        isName = (result.target == ErrorTarget.NAME || result.target ==  ErrorTarget.GENERAL),
                        isPassword = (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL),
                        nameError = if (result.target == ErrorTarget.NAME || result.target ==  ErrorTarget.GENERAL) result.message else "",
                        passwordError = if (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL) result.message else ""
                    )
                    return@launch
                }
            }
        }
    }
}