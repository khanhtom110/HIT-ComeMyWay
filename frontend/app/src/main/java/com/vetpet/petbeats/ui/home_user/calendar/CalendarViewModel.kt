package com.vetpet.petbeats.ui.home_user.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.CreateAppointmentRequest
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.TakeBookingRequest
import com.vetpet.petbeats.data.repository.ErrorTarget
import com.vetpet.petbeats.data.repository.HomeUserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CalendarViewModel(
    private val repository: HomeUserRepository
): ViewModel() {
    private var _state = MutableStateFlow(CalendarState())
    val state = _state.asStateFlow()

    private var _event = MutableSharedFlow< CalendarEvent>()
    val event = _event.asSharedFlow()

    fun informationClick(id: Int) {
        viewModelScope.launch {
            _event.emit(CalendarEvent.NavigationInformationRoom(id))
        }
    }

    fun onCancelAppointment() {
        viewModelScope.launch {
            _event.emit(CalendarEvent.NavigationNextRoom)
        }
    }

    fun onNameChange(name: String) {
        _state.value = _state.value.copy(name = name, isName = false)
    }

    fun onPhoneChange(phone: String) {
        _state.value = _state.value.copy(phone = phone, isPhone = false)
    }

    fun onAddressChange(address: String) {
        _state.value = _state.value.copy(address = address, isAddress = false)
    }

    fun onQuantityChange(quantity: Int) {
        _state.value = _state.value.copy(quantity = quantity, isQuantity = false)
    }

    fun onOtherChange(other: String) {
        _state.value = _state.value.copy(other = other, isInputOther = false)
    }

    fun onStateChange(state: String) {
        _state.value = _state.value.copy(state = state, isInputState = false)
    }

    fun onDogClick() {
        _state.value = _state.value.copy(isDog = true, isCat = false, isInputOther = false)
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
            val result = repository.takeBooking(request)

            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        id = data.id,
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

    fun onServiceOpen(id: Int) {
        val currentService = _state.value.selectService.toMutableList() //lấy id đang chọn

        if (!currentService.contains(id)) {
            currentService.add(id)

            _state.value = _state.value.copy(selectService = currentService)
        }

    }

    fun onServiceClose(id: Int) {
        val currentService = _state.value.selectService.toMutableList()

        if (currentService.contains(id)) {
            currentService.remove(id)

            _state.value = _state.value.copy(selectService = currentService)
        }
    }



    fun onTimeSelect(hour: String, minute: String) {
        _state.value = _state.value.copy(hour = hour, minute = minute)
    }

    fun onDateSelect(date: String) {
        _state.value = _state.value.copy(appointmentDate = date)
    }


    fun onCalendarClick(clinicId: Int) {
        viewModelScope.launch {
            if (_state.value.isDog) {
                _state.value = _state.value.copy(petType = "Chó")
            }
            if (_state.value.isCat) {
                _state.value = _state.value.copy(petType = "Mèo")
            }
            if (_state.value.isOther) {
                _state.value = _state.value.copy(petType = _state.value.other)
            }


            if (_state.value.isClinic) {
                _state.value = _state.value.copy(bookingType = "AT_CLINIC")
            }
            if (_state.value.isHome) {
                _state.value = _state.value.copy(bookingType = "AT_HOME")
            }


            val name = _state.value.name
            val phone = _state.value.phone
            val bookingType = _state.value.bookingType //lấy một trong clinic, home
            val address = _state.value.address
            val quantity = _state.value.quantity
            val petType = _state.value.petType //lấy một trong dog, cat, other
            val petCondition = _state.value.state
            val date = _state.value.appointmentDate
            val appointmentTime = "${_state.value.hour}:${_state.value.minute}"
            val service = _state.value.selectService

            Log.d("TEST_CASE", "name: ${name}, phone: ${phone}, bookingType ${bookingType}, address: ${address}, quantity: ${quantity}, petType: ${petType}, petCondition: ${petCondition}, date: ${date}, appointmentTime: ${appointmentTime}, service: ${service}")

            val request = CreateAppointmentRequest(clinicId,name, phone, bookingType, address, quantity, petType, petCondition, date, appointmentTime, service)
            val result = repository.createAppointment(request)

            when (result) {
                is DataResult.Success -> {
                    _state.value = _state.value.copy(isPhone = false, isInformation = false, isService = false, isCalendar = false, isTime = false)

                    _event.emit(CalendarEvent.NavigationSuccessAppointment(result.data.id, clinicId))
                }
                is DataResult.Error -> {
                    Log.d("TEST_CASE", "Mã lỗi: ${result.target} - Lý do: ${result.message}")
                    _state.value = _state.value.copy(
                        isName = (result.target == ErrorTarget.NAME),
                        isPhone = (result.target == ErrorTarget.PHONE),
                        isQuantity = (result.target == ErrorTarget.QUANTITY),
                        isInformation = (result.target == ErrorTarget.INFORMATION || result.target == ErrorTarget.GENERAL),
                        isService = (result.target == ErrorTarget.SERVICE || result.target == ErrorTarget.GENERAL),
                        isCalendar = (result.target == ErrorTarget.CALENDAR || result.target == ErrorTarget.GENERAL),
                        isTime = (result.target == ErrorTarget.TIME || result.target == ErrorTarget.GENERAL),
                        nameError = if (result.target == ErrorTarget.NAME || result.target == ErrorTarget.GENERAL) result.message else "",
                        phoneError = if (result.target == ErrorTarget.PHONE || result.target == ErrorTarget.GENERAL) result.message else "",
                        quantityError = if(result.target == ErrorTarget.QUANTITY) result.message else "",
                        informationError = if (result.target == ErrorTarget.INFORMATION || result.target == ErrorTarget.GENERAL) result.message else "",
                        serviceError = if (result.target == ErrorTarget.SERVICE || result.target == ErrorTarget.GENERAL) result.message else "",
                        calendarError = if (result.target == ErrorTarget.CALENDAR || result.target == ErrorTarget.GENERAL) result.message else "",
                        timeError = if (result.target == ErrorTarget.TIME || result.target == ErrorTarget.GENERAL) result.message else "",
                    )

                    return@launch
                }
            }

        }
    }


    fun onProfile() {
        viewModelScope.launch {
            val result = repository.profile()

            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        name = data.fullName.orEmpty(),
                        phone = data.phone.orEmpty(),
                        address = data.homeAddress.orEmpty(),
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