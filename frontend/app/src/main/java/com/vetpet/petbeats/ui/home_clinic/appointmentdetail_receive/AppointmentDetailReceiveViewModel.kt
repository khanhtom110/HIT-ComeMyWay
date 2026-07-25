package com.vetpet.petbeats.ui.home_clinic.appointmentdetail_receive

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.AppointmentIdRequest
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import com.vetpet.petbeats.ui.home_user.book.adapter.BookChildState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AppointmentDetailReceiveViewModel(
    private val repository: HomeClinicRepository
): ViewModel() {
    private val _state = MutableStateFlow(AppointmentDetailReceiveState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AppointmentDetailReceiveEvent>()
    val event = _event.asSharedFlow()

    fun appointmentScheduleClick() {
        viewModelScope.launch {
            _event.emit(AppointmentDetailReceiveEvent.NavigationAppointmentSchedule)
        }
    }

    fun onAppointmentDetailReceive(id: Int) {
        viewModelScope.launch {
            val request = AppointmentIdRequest(id)
            val result = repository.takeAppointmentId(request)

            when(result) {
                is DataResult.Success -> {
                    val data = result.data

                    val mapStatus = when (data.status) {
                        "PENDING" -> BookChildState.PENDING
                        "CONFIRMED" -> BookChildState.CONFIRMED
                        "REJECTED" -> BookChildState.REJECTED
                        else -> BookChildState.PENDING
                    }

                    val bookingType = when(data.bookingType) {
                        "AT_HOME" -> "Khám tại nhà"
                        "AT_CLINIC" -> "Khám tại phòng khám"
                        else -> data.bookingType
                    }

                    _state.value = _state.value.copy(
                        status = mapStatus,
                        imgPet = data.avatar.orEmpty(),
                        day = data.appointmentDate,
                        time = data.appointmentTime,
                        quantity = data.petQuantity,
                        bookingType = bookingType,
                        services = data.services,
                        fullName = data.fullName,
                        phone = data.phone,
                        address = data.homeAddress,
                        state = data.petCondition
                    )
                }
                is DataResult.Error -> {
                    Log.d("BUGG", "message:${result.message}")

                    _state.value = _state.value.copy()
                    return@launch
                }
            }
        }
    }
}