package com.vetpet.petbeats.ui.home_admin.add_clinic_success_admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddClinicSuccessAdminViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(AddClinicSuccessAdminState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AddClinicSuccessAdminEvent>()
    val event = _event.asSharedFlow()

    fun appointmentAdminClick() {
        viewModelScope.launch {
            _event.emit(AddClinicSuccessAdminEvent.NavigationListAppointmentAdmin)
        }
    }
}