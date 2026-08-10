package com.vetpet.petbeats.ui.home_user.confirmappointment

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.AppointmentIdRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.TakeBookingRequest
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.book.adapter.BookChildState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ConfirmAppointmentViewModel(
    private val repository: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow(ConfirmAppointmentState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ConfirmAppointmentEvent>()
    val event = _event.asSharedFlow()

    fun bookClick() {
        viewModelScope.launch {
            _event.emit(ConfirmAppointmentEvent.NavigationHomeAppointment)
        }
    }

    fun searchClick() {
        viewModelScope.launch {
            _event.emit(ConfirmAppointmentEvent.NavigationSearch)
        }
    }

    fun onEditAppointmentClick(id: Int, clinicId: Int) {
        viewModelScope.launch {
            _event.emit(ConfirmAppointmentEvent.NavigationEditAppointment(id, clinicId))
        }
    }

    //api này tự động lấy id của màn calendar và gắn id cho service(người dùng click) và hiển thị thông tin lên giao diện
    fun onInformationBookingAPI(clinicId: Int) {
        viewModelScope.launch {
            val request = TakeBookingRequest(clinicId)
            val result = repository.takeBooking(request)

            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        thumbnailUrl = data.thumbnailUrl,
                        name = data.name,
                        state = data.isOperating,
                        rating = data.rating,
                        phoneClinic = data.phone,
                        clinicAddress = data.address
                    )
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(thumbnailUrl = "", name = "", state = false, rating = 0.0, services = emptyList())
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

                    Log.d("TEST_API", "id: $id, data: ${data.fullName}, message: ${result.message}")

                    val mapStatus = when (data.status) {
                        "PENDING" -> BookChildState.PENDING
                        "CONFIRMED" -> BookChildState.CONFIRMED
                        "REJECTED" -> BookChildState.REJECTED
                        "CANCELLED" -> BookChildState.REJECTED
                        else -> BookChildState.PENDING
                    }

                    val bookingType = when(data.bookingType) {
                        "AT_HOME" -> "Khám tại nhà"
                        "AT_CLINIC" -> "Khám tại phòng khám"
                        else -> data.bookingType
                    }

                    _state.value = _state.value.copy(
                        fullName = data.fullName,
                        phoneUser = data.phone,
                        bookingType = bookingType,
                        homeAddress = data.homeAddress,
                        petType = data.petType,
                        petCondition = data.petCondition,
                        petQuantity = data.petQuantity,
                        appointmentDate = data.appointmentDate,
                        appointmentTime = data.appointmentTime,
                        services = data.services,
                        status = mapStatus,
                        reason = data.rejectReason ?: "",
                    )
                }
                is DataResult.Error -> {
                    Log.d("TEST_CASE", "lỗi trả: ${result.target} và message ${result.message}")

                    _state.value = _state.value.copy()
                    return@launch
                }
            }
        }
    }
}