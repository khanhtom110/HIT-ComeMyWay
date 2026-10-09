package com.vetpet.petbeats.ui.home_admin.add_clinic_admin

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.CreateClinicRequest
import com.vetpet.petbeats.data.repository.repository_springboot.AuthAdminRepository
import com.vetpet.petbeats.data.repository.repository_springboot.ErrorTarget
import com.vetpet.petbeats.ui.auth.login.LoginEvent
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddClinicAdminViewModel @Inject constructor(
    private val repository: AuthAdminRepository
): ViewModel() {
    private val _state = MutableStateFlow(AddClinicAdminState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AddClinicAdminEvent>()
    val event = _event.asSharedFlow()


    fun listAppointmentAdminClick() {
        viewModelScope.launch {
            _event.emit(AddClinicAdminEvent.NavigationListAppointmentAdmin)
        }
    }



    fun onNameChange(name: String) {
        viewModelScope.launch {
            _state.value =_state.value.copy(name = name, isName = false)
        }
    }

    fun onPasswordChange(password: String) {
        viewModelScope.launch {
            _state.value =_state.value.copy(email = password, isEmail = false)
        }
    }

    fun onAddAccountClick() {
        viewModelScope.launch {
            val name = _state.value.name.trim()
            val email = _state.value.email.trim()

            if (name.isEmpty() || email.isEmpty()) {
                _state.value = _state.value.copy(isName = true, isEmail = true, nameError = "Vui lòng nhập đầy đủ thông tin", emailError = "Vui lòng nhập đầy đủ thông tin")
                return@launch
            }
            else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                _state.value = _state.value.copy(isName = false, isEmail = true, emailError = "Nhập không đúng email. Nhập lại!")
                return@launch
            }



            val request = CreateClinicRequest(name, email)
            val result = repository.createClinic(request)

            when (result) {
                is DataResult.Success -> {
                    _state.value = _state.value.copy(isName = false, isEmail = false, nameError = "", emailError = "")
                    _event.emit(AddClinicAdminEvent.NavigationAddClinicSuccess)
                }

                is DataResult.Error -> {
                    _state.value = _state.value.copy(
                        isName = (result.target == ErrorTarget.NAME || result.target ==  ErrorTarget.GENERAL),
                        isEmail = (result.target == ErrorTarget.EMAIL || result.target ==  ErrorTarget.GENERAL),
                        nameError = if (result.target == ErrorTarget.NAME || result.target ==  ErrorTarget.GENERAL) result.message else "",
                        emailError = if (result.target == ErrorTarget.EMAIL || result.target ==  ErrorTarget.GENERAL) result.message else ""
                    )
                    return@launch
                }
            }
        }
    }
}