package com.vetpet.petbeats.ui.home_clinic.informationclinic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.dto.calendar.home_clinic.request.ProfileRequest
import com.vetpet.petbeats.data.repository.ErrorTarget
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InformationClinicViewModel(
    private val repository: HomeClinicRepository
): ViewModel() {
    private var _state = MutableStateFlow(InformationClinicState())
    val state = _state.asStateFlow()

    private var _event = MutableSharedFlow<InformationClinicEvent>()
    val event = _event.asSharedFlow()



    fun onNameChange(name: String) {
        _state.value = _state.value.copy(name = name, isName = false)
    }

    fun onPhoneChange(phone: String) {
        _state.value = _state.value.copy(phone = phone, isPhone = false)
    }

    fun onAddressChange(address: String) {
        _state.value = _state.value.copy(address = address, isAddress = false)
    }

    fun onStateChange(state: String) {
        _state.value = _state.value.copy(state = state, isInputState = false)
    }

    fun onLinkChange(link: String) {
        _state.value = _state.value.copy(link = link, isLink = false)
    }

    fun onTimeOpenSelect(openHour: String, openMinute: String) {
        _state.value = _state.value.copy(openHour = openHour, openMinute = openMinute)
    }

    fun onTimeCloseSelect(closeHour: String, closeMinute: String) {
        _state.value = _state.value.copy(closeHour = closeHour, closeMinute = closeMinute)
    }

    fun onInformationClinicClick() {
        viewModelScope.launch {
            if (_state.value.isClinic) {
                _state.value = _state.value.copy(bookingType = "AT_CLINIC")
            }
            if (_state.value.isHome) {
                _state.value = _state.value.copy(bookingType = "AT_HOME")
            }

            val image = _state.value.image

            val name = _state.value.name
            val phone = _state.value.phone
            val address = _state.value.address
            val link = _state.value.link
            val openTime = "${_state.value.openHour}:${_state.value.openMinute}"
            val closeTime = "${_state.value.closeHour}:${_state.value.closeMinute}"
            val state = _state.value.state

            val bookingType = _state.value.bookingType //lấy một trong clinic, home
            val service = _state.value.services


            val request = ProfileRequest(name, address, link, phone, state, image, openTime, closeTime, service)
            val result = repository.completeProfile(request)

            when (result) {
                is DataResult.Success -> {
                    _state.value = _state.value.copy(isName = false, isAddress = false, isLink = false, isPhone = false, isInputState = false, isImage = false, isTime = false, isService = false)

                    _event.emit(InformationClinicEvent.NavigationInformationSuccess)
                }
                is DataResult.Error -> {
                    Log.d("TEST_CASE", "Mã lỗi: ${result.target} - Lý do: ${result.message}")
                    _state.value = _state.value.copy(
                        isName = (result.target == ErrorTarget.NAME || result.target == ErrorTarget.GENERAL),
                        isPhone = (result.target == ErrorTarget.PHONE || result.target == ErrorTarget.GENERAL),
                        isLink = (result.target == ErrorTarget.LINK || result.target == ErrorTarget.GENERAL),
                        isInformation = (result.target == ErrorTarget.INFORMATION || result.target == ErrorTarget.GENERAL),
                        isTime = (result.target == ErrorTarget.TIME || result.target == ErrorTarget.GENERAL),
                        isService = (result.target == ErrorTarget.SERVICE || result.target == ErrorTarget.GENERAL),
                        nameError = if (result.target == ErrorTarget.NAME || result.target == ErrorTarget.GENERAL) result.message else "",
                        phoneError = if (result.target == ErrorTarget.PHONE || result.target == ErrorTarget.GENERAL) result.message else "",
                        linkError = if (result.target == ErrorTarget.LINK || result.target == ErrorTarget.GENERAL) result.message else "",
                        informationError = if (result.target == ErrorTarget.INFORMATION || result.target == ErrorTarget.GENERAL) result.message else "",
                        timeError = if (result.target == ErrorTarget.TIME || result.target == ErrorTarget.GENERAL) result.message else "",
                        serviceError = if (result.target == ErrorTarget.SERVICE || result.target == ErrorTarget.GENERAL) result.message else "",
                    )

                    return@launch
                }
            }
        }
    }
}