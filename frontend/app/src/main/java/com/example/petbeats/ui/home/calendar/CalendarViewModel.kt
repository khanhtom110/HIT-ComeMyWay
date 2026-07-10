package com.example.petbeats.ui.home.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.remote.model.calendar.home.request.CreateAppointmentRequest
import com.example.petbeats.data.remote.model.calendar.home.request.TakeBookingRequest
import com.example.petbeats.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val repository: HomeRepository
): ViewModel() {
    private var _state = MutableStateFlow(CalendarState())
    val state = _state.asStateFlow()

    private var _event = MutableSharedFlow< CalendarEvent>()
    val event = _event.asSharedFlow()

    fun InformationClick(id: Int) {
        viewModelScope.launch {
            _event.emit(CalendarEvent.NavigationInformationRoom(id))
        }
    }

    fun onNameChange(name: String) {
        _state.value = _state.value.copy(name = name, isName = true)
    }

    fun onPhoneChange(phone: String) {
        _state.value = _state.value.copy(phone = phone, isPhone = true)
    }

    fun onAddressChange(address: String) {
        _state.value = _state.value.copy(address = address, isAddress = true)
    }

    fun onQuantityChange(quantity: String) {
        _state.value = _state.value.copy(quantity = quantity, isQuantity = true)
    }

    fun onOtherChange(other: String) {
        _state.value = _state.value.copy(other = other, isInputOther = true)
    }

    fun onStateChange(state: String) {
        _state.value = _state.value.copy(state = state, isInputState = true)
    }

    fun onDogClick() {
        _state.value = _state.value.copy(isDog = true, isCat = false, isOther = false)
    }

    fun onCatClick() {
        _state.value = _state.value.copy(isDog = false, isCat = true, isOther = false)
    }

    fun onOtherClick() {
        _state.value = _state.value.copy(isDog = false, isCat = false, isOther = true)
    }

    fun onClinicClick() {
        _state.value = _state.value.copy(isClinic = true, isHome = false)
    }

    fun onHomeClick() {
        _state.value = _state.value.copy(isClinic = false, isHome = true)
    }

    //api này tự động lấy id của màn information và gắn id cho service(người dùng click) và hiển thị thông tin lên giao diện
    fun onInformationBookingAPI(id: Int) {
        viewModelScope.launch {
            val request = TakeBookingRequest(id)
            val result = repository.takeBook(request)

            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        thumbnailUrl = data.thumbnailUrl,
                        tittle = data.name,
                        isOperating = data.isOperating,
                        rating = data.rating,
                        services = data.services
                    )
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(thumbnailUrl = "", name = "", isOperating = false, rating = 0.0, services = emptyList())
                    return@launch
                }
            }
        }
    }


    fun onCalendarClick(id: Int) {
        viewModelScope.launch {
            val name = _state.value.name
            val phone = _state.value.phone
            val address = _state.value.address
            val quantity = _state.value.quantity

            _event.emit(CalendarEvent.NavigationConfirmAppointment(id))

//            val request = CreateAppointmentRequest()
//            val result = repository.createAppointment(request)
//
//            when (result) {
//                is DataResult.Success -> {
//
//                }
//                is DataResult.Error -> {
//
//                }
//            }

        }
    }
}