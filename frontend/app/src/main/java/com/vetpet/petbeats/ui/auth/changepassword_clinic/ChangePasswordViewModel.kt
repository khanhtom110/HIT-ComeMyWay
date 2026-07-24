package com.vetpet.petbeats.ui.auth.changepassword_clinic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ChangePasswordRequest
import com.vetpet.petbeats.data.repository.ErrorTarget
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChangePasswordViewModel(
    private val repository: HomeClinicRepository
): ViewModel() {
    private val _state = MutableStateFlow(ChangePasswordState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ChangePasswordEvent>()
    val event = _event.asSharedFlow()

    fun changePassword() {
        _state.value = _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)
    }

    fun changePassword1() {
        _state.value = _state.value.copy(isPasswordVisible1 = !_state.value.isPasswordVisible1)
    }

    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(password = password, isPassword = false)
    }

    fun onPasswordChange1(password1: String) {
        _state.value = _state.value.copy(password1 = password1, isPassword1 = false)
    }

    fun onChangeClick() {
        viewModelScope.launch {
            val password = _state.value.password.trim()
            val password1 = _state.value.password1.trim()



            val request = ChangePasswordRequest(password, password1)
            val result = repository.changePassword(request)


            when (result) {
                is DataResult.Success -> {
                    _state.value = _state.value.copy(isPassword = false, isPassword1 = false, passwordError = "", passwordError1 = "")

                    Log.d("TEST_CASE", "message: ${result.message}")

                    _event.emit(ChangePasswordEvent.NavigationChangeSuccess)
                }

                is DataResult.Error -> {
                    Log.d("TEST_CASE", "message: ${result.message}, target = ${result.target}")

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