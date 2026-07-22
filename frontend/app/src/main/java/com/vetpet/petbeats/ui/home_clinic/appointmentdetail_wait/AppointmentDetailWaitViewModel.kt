package com.vetpet.petbeats.ui.home_clinic.appointmentdetail_wait

import androidx.lifecycle.ViewModel
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class AppointmentDetailWaitViewModel(
    private val repository: HomeClinicRepository
): ViewModel() {
    private val _state = MutableStateFlow(AppointmentDetailWaitState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AppointmentDetailWaitEvent>()
    val event = _event.asSharedFlow()
}