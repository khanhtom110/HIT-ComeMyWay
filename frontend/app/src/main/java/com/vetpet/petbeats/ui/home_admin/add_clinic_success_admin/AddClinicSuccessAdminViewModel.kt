package com.vetpet.petbeats.ui.home_admin.add_clinic_success_admin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class AddClinicSuccessAdminViewModel: ViewModel() {
    private val _state = MutableStateFlow(AddClinicSuccessAdminState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AddClinicSuccessAdminEvent>()
    val event = _event.asSharedFlow()
}