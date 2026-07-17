package com.example.petbeats.ui.home.successAppointment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.remote.model.calendar.home.request.AppointmentIdRequest
import com.example.petbeats.data.remote.model.calendar.home.request.DeviceTokenFireBaseRequest
import com.example.petbeats.data.remote.model.calendar.home.request.TakeBookingRequest
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.ui.home.book.adapter.BookChildState
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

    fun sendDeviceToken(token: String) {
        viewModelScope.launch {
            val request = DeviceTokenFireBaseRequest(token)
            repository.deviceToken(request)
        }
    }


    fun onSuccessInformation(id: Int, clinicId: Int) {
        viewModelScope.launch {
            val id = AppointmentIdRequest(id)
            val clinicId = TakeBookingRequest(clinicId)
            val resultClinicId = repository.takeAppointmentId(id)
            val resultId = repository.takeBooking(clinicId)

            when (resultClinicId) {
                is DataResult.Success -> {
                    val data = resultClinicId.data

                    val mapStatus = when (data.status) {
                        "PENDING" -> BookChildState.PENDING
                        "SUCCESS" -> BookChildState.SUCCESS
                        "CANCELLED" -> BookChildState.CANCELLED
                        else -> BookChildState.PENDING
                    }

                    _state.value = _state.value.copy(
                        status = mapStatus,
                        date = "${data.appointmentDate} -",
                        time = data.appointmentTime
                    )
                }
                is DataResult.Error ->  {
                    return@launch
                }
            }

            when (resultId) {
                is DataResult.Success -> {
                    val data = resultId.data

                    _state.value = _state.value.copy(
                        address = data.address
                    )
                }
                is DataResult.Error -> {
                    return@launch
                }
            }
        }
    }
}