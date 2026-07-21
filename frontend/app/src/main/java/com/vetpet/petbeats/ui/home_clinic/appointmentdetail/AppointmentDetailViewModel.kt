package com.vetpet.petbeats.ui.home_clinic.appointmentdetail

import androidx.lifecycle.ViewModel
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class AppointmentDetailViewModel(
    private val repository: HomeClinicRepository
): ViewModel() {
    private val _state = MutableStateFlow(AppointmentDetailState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AppointmentDetailEvent>()
    val event = _event.asSharedFlow()
}