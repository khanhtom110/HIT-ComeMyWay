package com.example.petbeats.ui.home.successAppointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SuccessAppointmentViewModel(
    private val repository: HomeRepository
): ViewModel() {
    private val _state = MutableStateFlow(SuccessAppointmentState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<SuccessAppointmentEvent>()
    val event = _event.asSharedFlow()

    fun bookingClick() {
        viewModelScope.launch {
            _event.emit(SuccessAppointmentEvent.NavigationBooking)
        }
    }

    fun searchClick() {
        viewModelScope.launch {
            _event.emit(SuccessAppointmentEvent.NavigationSearch)
        }
    }

    fun confirmClick(id: Int, clinicId: Int) {
        viewModelScope.launch {
            _event.emit(SuccessAppointmentEvent.NavigationConfirm(id, clinicId))
        }
    }
}