package com.example.petbeats.ui.auth.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.repository.AuthRepository
import com.example.petbeats.data.repository.ErrorTarget
import com.example.petbeats.ui.auth.forgotpassword.ForgotPasswordEvent
import com.example.petbeats.ui.auth.forgotpassword.request_response.ForgotPasswordRequest
import com.example.petbeats.ui.auth.otp.request_response.OtpRequest
import com.example.petbeats.ui.auth.register.request_response.RegisterRequest
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

    fun input1(otp: String) {
        _state.value = _state.value.copy(otp1 = otp)
    }

    fun input2(otp: String) {
        _state.value = _state.value.copy(otp2 = otp)
    }

    fun input3(otp: String) {
        _state.value = _state.value.copy(otp3 = otp)
    }

    fun input4(otp: String) {
        _state.value = _state.value.copy(otp4 = otp)
    }

    fun input5(otp: String) {
        _state.value = _state.value.copy(otp5 = otp)
    }

    fun input6(otp: String) {
        _state.value = _state.value.copy(otp6 = otp)
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
                    _event.emit(OtpEvent.NavigationSendToken(refreshToken))
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