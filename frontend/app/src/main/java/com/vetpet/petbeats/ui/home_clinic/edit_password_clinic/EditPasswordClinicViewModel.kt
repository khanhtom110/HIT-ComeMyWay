package com.vetpet.petbeats.ui.home_clinic.edit_password_clinic

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.ChangePasswordUserRequest
import com.vetpet.petbeats.data.repository.ErrorTarget
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class EditPasswordClinicViewModel(
    private val repository: HomeClinicRepository
): ViewModel() {
    private val _state = MutableStateFlow(EditPasswordClinicState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<EditPasswordClinicEvent>()
    val event = _event.asSharedFlow()


    fun forgotPassword() {
        viewModelScope.launch {
            _event.emit(EditPasswordClinicEvent.NavigationForgotPassword)
        }
    }
    fun clinicClick() {
        viewModelScope.launch {
            _event.emit(EditPasswordClinicEvent.NavigationClinic)
        }
    }



    fun changePassword() {
        _state.value = _state.value.copy(isPasswordVisible = !_state.value.isPasswordVisible)
    }
    fun changeNewPassword() {
        _state.value = _state.value.copy(isNewPasswordVisible = !_state.value.isNewPasswordVisible)
    }
    fun changeNewPassword1() {
        _state.value = _state.value.copy(isNewPasswordVisible1 = !_state.value.isNewPasswordVisible1)
    }



    fun onPasswordChange(password: String) {
        _state.value = _state.value.copy(password = password, isPassword = false)
    }
    fun onNewPasswordChange(newPassword: String) {
        _state.value = _state.value.copy(newPassword = newPassword, isNewPassword = false)
    }
    fun onNewPasswordChange1(newPassword1: String) {
        _state.value = _state.value.copy(newPassword1 = newPassword1, isNewPassword1 = false)
    }



    fun onEditPasswordClick() {
        viewModelScope.launch {
            val password = _state.value.password.trim()
            val newPassword = _state.value.newPassword.trim()
            val newPassword1 = _state.value.newPassword1.trim()

//            val request = ChangePasswordUserRequest(password, newPassword, newPassword1)
//            val result = repository.changePasswordUser(request)
//
//
//            when (result) {
//                is DataResult.Success -> {
//                    _state.value = _state.value.copy(isPassword = false, isNewPassword = false, isNewPassword1 = false, passwordError = "")
//
//                    _event.emit(EditPasswordClinicEvent.NavigationClinic)
//                }
//
//                is DataResult.Error -> {
//                    _state.value = _state.value.copy(
//                        isPassword = (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL),
//                        isNewPassword = (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL),
//                        isNewPassword1 = (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL),
//                        passwordError = if (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL) result.message else "",
//                        passwordNewError = if (result.target == ErrorTarget.PASSWORD || result.target ==  ErrorTarget.GENERAL) result.message else "",
//                    )
//                    return@launch
//                }
//            }
        }
    }

}
