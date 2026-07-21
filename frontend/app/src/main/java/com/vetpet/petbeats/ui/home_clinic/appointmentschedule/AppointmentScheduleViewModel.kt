package com.vetpet.petbeats.ui.home_clinic.appointmentschedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppointmentScheduleViewModel(
    private val repository: HomeClinicRepository
): ViewModel() {
    private val _state = MutableStateFlow(AppointmentScheduleState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AppointmentScheduleEvent>()
    val event = _event.asSharedFlow()

    fun scheduleToday() {
        viewModelScope.launch {
            _event.emit(AppointmentScheduleEvent.NavigationScheduleToday)
        }
    }

    fun scheduleList() {
        viewModelScope.launch {
            _event.emit(AppointmentScheduleEvent.NavigationScheduleList)
        }
    }


}