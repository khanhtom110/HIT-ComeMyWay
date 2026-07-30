package com.vetpet.petbeats.ui.auth.forgotpassword_user

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.repository.AuthRepository
import com.vetpet.petbeats.data.repository.ErrorTarget
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.ForgotPasswordRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.annotation.meta.When

class ForgotPasswordViewModel(
    private val repository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(ForgotPasswordState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow< ForgotPasswordEvent>()
    val event = _event.asSharedFlow()

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(email = email, isEmail = false)
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
            val result = repository.forgotPasswordUser(request)


            when (result) {
                is DataResult.Success -> {
                    val roles = result.data.role
                    _state.value = _state.value.copy(isEmail = false, emailError = "")

                    when (roles) {
                        "USER" -> {
                            _event.emit(ForgotPasswordEvent.NavigationOTPSendEmail(email))
                        }
                        "CLINIC" -> {
                            _event.emit(ForgotPasswordEvent.NavigationForgotClinicSuccess)
                        }
                    }

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