package com.example.petbeats.ui.home.confirmappointment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.remote.model.calendar.home.request.AppointmentIdRequest
import com.example.petbeats.data.remote.model.calendar.home.request.TakeBookingRequest
import com.example.petbeats.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConfirmAppointmentViewModel(
    private val repository: HomeRepository
): ViewModel() {
    private val _state = MutableStateFlow(ConfirmAppointmentState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ConfirmAppointmentEvent>()
    val event = _event.asSharedFlow()

    fun calendarClick(id: Int) {
        viewModelScope.launch {
            _event.emit(ConfirmAppointmentEvent.NavigationCalendar(id))
        }
    }

    //api này tự động lấy id của màn calendar và gắn id cho service(người dùng click) và hiển thị thông tin lên giao diện
    fun onInformationBookingAPI(id: Int) {
        viewModelScope.launch {
            val request = TakeBookingRequest(id)
            val result = repository.takeBooking(request)

            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        thumbnailUrl = data.thumbnailUrl,
                        name = data.name,
                        status = data.isOperating,
                        rating = data.rating,
                        services = data.services
                    )
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(thumbnailUrl = "", name = "", status = false, rating = 0.0, services = emptyList())
                    return@launch
                }
            }
        }
    }

    fun onInformationAppointment(id: Int) {
        viewModelScope.launch {
            val request = AppointmentIdRequest(id)
            val result = repository.takeAppointmentId(request)

            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        fullName = data.fullName,
                        phoneUser = data.phoneUser,
                        phoneClinic = data.phoneClinic,
                        homeAddress = data.homeAddress,
                        petType = data.petType,
                        petCondition = data.petCondition,
                        petQuantity = data.petQuantity,
                        appointmentDate = data.appointmentDate,
                        appointmentTime = data.appointmentTime,
                    )
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy()
                    return@launch
                }
            }
        }
    }

    fun onConfirmAppointment() {
        viewModelScope.launch {
            _event.emit(ConfirmAppointmentEvent.NavigationSuccessAppointment)
        }
    }
}