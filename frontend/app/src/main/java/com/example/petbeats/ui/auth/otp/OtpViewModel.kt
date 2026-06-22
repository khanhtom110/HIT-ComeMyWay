package com.example.petbeats.ui.auth.otp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.data.repository.AuthRepository
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

    fun forgotClick() {
        viewModelScope.launch {
            _event.emit(OtpEvent.NavigationForgot)
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

    fun onResetClick(email: String) {
        val otp = _state.value.otp1 + _state.value.otp2 + _state.value.otp3 + _state.value.otp4 + _state.value.otp5 + _state.value.otp6

        if (otp.length == 6) {
            _state.value = _state.value.copy(isOtp = true, otpError = "Mã xác minh không chính xác, vui lòng thử lại")
        }

        viewModelScope.launch {
            _event.emit(OtpEvent.NavigationResetSendEmail(email))
        }
    }

}