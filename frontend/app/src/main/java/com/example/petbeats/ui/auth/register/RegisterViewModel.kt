package com.example.petbeats.ui.auth.register

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.repository.AuthRepository
import com.example.petbeats.data.repository.ErrorTarget
import com.example.petbeats.data.remote.model.calendar.auth.request.RegisterRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class RegisterViewModel(
    private val repository: AuthRepository
): ViewModel() {
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
        _state.value = _state.value.copy(name = name, isName = false)
    }

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(email = email, isEmail = false)
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(password = password, isPassword = false)
    }

    fun onPasswordChange1(password1: String) {
        _state.value = _state.value.copy(password1 = password1, isPassword1 = false)
    }

    fun onOtpClick() {
        viewModelScope.launch {
            val name = _state.value.name.trim()
            val email = _state.value.email.trim()
            val password = _state.value.password.trim()
            val password1 = _state.value.password1.trim()



//            if (password.length < 7 || password1.length < 7) {
//                _state.value = _state.value.copy(isName = false, isEmail = false, isPassword = true, isPassword1 = true, nameError = "", emailError = "", passwordError = "Mật khẩu chưa đủ mạnh (cần chữ hoa, chữ thường, số và \nký tự đặc biệt).", passwordError1 = "Mật khẩu chưa đủ mạnh (cần chữ hoa, chữ thường, số và \nký tự đặc biệt).")
//                return@launch
//            }


            val request = RegisterRequest(name, email, password, password1)
            val result = repository.registerUser(request)

            when (result) {
                is DataResult.Success -> {
                    _state.value = _state.value.copy(isName = false, isEmail = false, isPassword = false, isPassword1 = false, nameError = "", emailError = "", passwordError = "", passwordError1 = "")

                    _event.emit(RegisterEvent.NavigationRegisterSendEmail(email))
                }

                is DataResult.Error -> {
                    _state.value = _state.value.copy(
                        isName = (result.target == ErrorTarget.NAME || result.target ==  ErrorTarget.GENERAL),
                        isEmail = (result.target == ErrorTarget.EMAIL || result.target ==  ErrorTarget.GENERAL),
                        isPassword = (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL),
                        isPassword1 = (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL),
                        nameError = if (result.target == ErrorTarget.NAME || result.target ==  ErrorTarget.GENERAL) result.message else "",
                        emailError = if (result.target == ErrorTarget.EMAIL || result.target ==  ErrorTarget.GENERAL) result.message else "",
                        passwordError = if (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL)  result.message else "",
                        passwordError1 = if (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL) result.message else ""
                    )
                    return@launch
                }
            }
        }
    }
}