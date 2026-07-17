package com.example.petbeats.ui.auth.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.repository.AuthRepository
import com.example.petbeats.data.repository.ErrorTarget
import com.example.petbeats.ui.auth.forgotpassword.ForgotPasswordEvent
import com.example.petbeats.data.remote.model.calendar.auth.request.ForgotPasswordRequest
import com.example.petbeats.data.remote.model.calendar.auth.request.OtpRequest
import com.example.petbeats.data.remote.model.calendar.auth.request.RegisterRequest
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class OtpViewModel(
    private val repository: AuthRepository
): ViewModel() {
    private val _state = MutableStateFlow(OtpState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<OtpEvent>()
    val event = _event.asSharedFlow()

    fun vectorClick() {
        viewModelScope.launch {
            _event.emit(OtpEvent.NavigationVector)
        }
    }

    fun otpClick(email: String) {
        viewModelScope.launch {
            val request = ForgotPasswordRequest(email)
            repository.forgotpasswordUser(request)
        }
    }

    fun input1(otp: String) {
        _state.value = _state.value.copy(otp1 = otp, isOtp = false)
    }

    fun input2(otp: String) {
        _state.value = _state.value.copy(otp2 = otp, isOtp = false)
    }

    fun input3(otp: String) {
        _state.value = _state.value.copy(otp3 = otp, isOtp = false)
    }

    fun input4(otp: String) {
        _state.value = _state.value.copy(otp4 = otp, isOtp = false)
    }

    fun input5(otp: String) {
        _state.value = _state.value.copy(otp5 = otp, isOtp = false)
    }

    fun input6(otp: String) {
        _state.value = _state.value.copy(otp6 = otp, isOtp = false)
    }

    fun onResetClick(email: String, currentScreen: String) {
        viewModelScope.launch {
            val otp = _state.value.otp1 + _state.value.otp2 + _state.value.otp3 + _state.value.otp4 + _state.value.otp5 + _state.value.otp6


            val request = OtpRequest(email, otp)
            val result =
                if (currentScreen == "registersuccess") {
                    repository.registerOtpUser(request)
                }
                else {
                    repository.resetOtpUser(request)
                }


            when (result) {
                is DataResult.Success -> {
                    _state.value = _state.value.copy(isOtp = false, otpError = "")

                    val refreshToken = result.data.token ?: ""
                    _event.emit(OtpEvent.NavigationSendToken(refreshToken, email))
                }

                is DataResult.Error -> {
                    _state.value = _state.value.copy(
                        isOtp = (result.target == ErrorTarget.OTP || result.target ==  ErrorTarget.GENERAL),
                        otpError = if (result.target == ErrorTarget.OTP || result.target ==  ErrorTarget.GENERAL) result.message else ""
                    )
                    return@launch
                }
            }
        }
    }

}