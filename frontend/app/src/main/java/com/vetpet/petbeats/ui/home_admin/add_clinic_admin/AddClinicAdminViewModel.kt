package com.vetpet.petbeats.ui.home_admin.add_clinic_admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AddClinicAdminViewModel: ViewModel() {
    private val _state = MutableStateFlow(AddClinicAdminState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AddClinicAdminEvent>()
    val event = _event.asSharedFlow()


    fun listAppointmentAdminClick() {
        viewModelScope.launch {
            _event.emit(AddClinicAdminEvent.NavigationListAppointmentAdmin)
        }
    }
}