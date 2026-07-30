package com.vetpet.petbeats.ui.auth.forgotpassword_clinic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

class ForgotPasswordClinicViewModel: ViewModel() {

    private val _event = MutableSharedFlow<ForgotPasswordClinicEvent>()
    val event = _event.asSharedFlow()

    fun forgotClick() {
        viewModelScope.launch {
            _event.emit(ForgotPasswordClinicEvent.NavigationForgotPassword)
        }
    }

    fun loginClick() {
        viewModelScope.launch {
            _event.emit(ForgotPasswordClinicEvent.NavigationLogin)
        }
    }
}