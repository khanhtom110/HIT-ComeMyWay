package com.vetpet.petbeats.ui.home_clinic.appointmentdetai_refuse

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_clinic.request.ReasonRejectRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.AppointmentIdRequest
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import com.vetpet.petbeats.ui.home_clinic.appointmentdetail_receive.AppointmentDetailReceiveEvent
import com.vetpet.petbeats.ui.home_user.book.adapter.BookChildState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppointmentDetailRefuseViewModel(
    private val repository: HomeClinicRepository
): ViewModel() {
    private val _state = MutableStateFlow(AppointmentDetailRefuseState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AppointmentDetailRefuseEvent>()
    val event = _event.asSharedFlow()

    fun appointmentScheduleClick() {
        viewModelScope.launch {
            _event.emit(AppointmentDetailRefuseEvent.NavigationAppointmentSchedule)
        }
    }

    fun onAppointmentDetailRefuse(id: Int) {
        viewModelScope.launch {
            val requestId = AppointmentIdRequest(id)
            val result = repository.takeAppointmentId(requestId)

            when(result) {
                is DataResult.Success -> {
                    val data = result.data

                    val mapStatus = when (data.status) {
                        "PENDING" -> BookChildState.PENDING
                        "CONFIRMED" -> BookChildState.CONFIRMED
                        "REJECTED" -> BookChildState.REJECTED
                        else -> BookChildState.PENDING
                    }

                    _state.value = _state.value.copy(
                        status = mapStatus,
                        imgPet = data.avatar.orEmpty(),
                        day = data.appointmentDate,
                        time = data.appointmentTime,
                        quantity = data.petQuantity,
                        bookingType = data.bookingType,
                        services = data.services,
                        fullName = data.fullName,
                        phone = data.phone,
                        address = data.homeAddress,
                        state = data.petCondition,
                        reason = data.rejectReason
                    )
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy()
                    return@launch
                }
            }
        }
    }
}