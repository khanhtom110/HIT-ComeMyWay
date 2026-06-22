package com.example.petbeats.ui.auth.forgotpassword

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ForgotPasswordViewModel(
    private val repository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(ForgotPasswordState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow< ForgotPasswordEvent>()
    val event = _event.asSharedFlow()

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(email = email)
    }

    fun loginClick() {
        viewModelScope.launch {
            _event.emit(ForgotPasswordEvent.NavigationLogin)
        }
    }

    fun onOtpClick() {
        viewModelScope.launch {
            val email = _state.value.email.trim()

            if (email.isEmpty()) {
                _state.value = _state.value.copy(isEmail = true, emailError = "Vui lòng nhập đầy đủ thông tin")
                return@launch
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _state.value = _state.value.copy(isEmail = true, emailError = "Nhập không đúng email. Nhập lại!")
                return@launch
            }

            _event.emit(ForgotPasswordEvent.NavigationOTPSendEmail(email))
        }
    }
}