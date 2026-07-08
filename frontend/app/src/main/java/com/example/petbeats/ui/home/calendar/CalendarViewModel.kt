package com.example.petbeats.ui.home.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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

    fun InformationClick() {
        viewModelScope.launch {
            _event.emit(CalendarEvent.NavigationInformationRoom)
        }
    }

    fun changeName() {
        _state.value = _state.value.copy(isName = !_state.value.isName)
    }

    fun changePhone() {
        _state.value = _state.value.copy(isPhone = !_state.value.isPhone)
    }

    fun changeAddress() {
        _state.value = _state.value.copy(isAddress = !_state.value.isAddress)
    }

    fun changeQuantity() {
        _state.value = _state.value.copy(isQuantity = !_state.value.isQuantity)
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

    fun onDogClick() {
        _state.value = _state.value.copy(isDog = true, isCat = false, isOther = false)
    }

    fun onCatClick() {
        _state.value = _state.value.copy(isDog = false, isCat = true, isOther = false)
    }

    fun onOtherClick() {
        _state.value = _state.value.copy(isDog = false, isCat = false, isOther = true)
    }



    fun onCalendarClick() {
        viewModelScope.launch {
            val name = _state.value.name
            val phone = _state.value.phone
            val address = _state.value.address
            val quantity = _state.value.quantity

            if (name.isEmpty() || phone.isEmpty() || address.isEmpty() || quantity.isEmpty()) {
                _state.value = _state.value.copy(isInformation = true, informationError = "Vui lòng nhập đầy đủ thông tin bắt buộc")
            }


        }
    }
}