package com.example.petbeats.ui.auth.forgotpassword

import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.repository.AuthRepository
import com.example.petbeats.data.repository.ErrorTarget
import com.example.petbeats.ui.auth.forgotpassword.request_response.ForgotPasswordRequest
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




            val request = ForgotPasswordRequest(email)
            val result = repository.forgotpasswordUser(request)

            when (result) {
                is DataResult.Success -> {
                    _state.value = _state.value.copy(isEmail = false, emailError = "")

                    _event.emit(ForgotPasswordEvent.NavigationOTPSendEmail(email))
                }

                is DataResult.Error -> {
                    _state.value = _state.value.copy(
                        isEmail = (result.target == ErrorTarget.EMAIL || result.target ==  ErrorTarget.GENERAL),
                        emailError = if (result.target == ErrorTarget.EMAIL || result.target ==  ErrorTarget.GENERAL) result.message else ""
                    )
                    return@launch
                }
            }
        }
    }
}