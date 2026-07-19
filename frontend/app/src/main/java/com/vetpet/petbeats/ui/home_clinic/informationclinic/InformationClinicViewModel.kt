package com.vetpet.petbeats.ui.home_clinic.informationclinic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.dto.calendar.home.request.CreateAppointmentRequest
import com.vetpet.petbeats.data.repository.ErrorTarget
import com.vetpet.petbeats.data.repository.HomeRepository
import com.vetpet.petbeats.ui.home_user.calendar.CalendarEvent
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InformationClinicViewModel(
    private val repository: HomeRepository
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

//    fun onInformationClinicClick() {
//        viewModelScope.launch {
//            val name = _state.value.name
//            val phone = _state.value.phone
//            val address = _state.value.address
//            val petCondition = _state.value.state
//            val link = _state.value.link
//
//            val request = CreateAppointmentRequest()
//            val result = repository.createAppointment(request)
//
//            when (result) {
//                is DataResult.Success -> {
//                    _state.value = _state.value.copy()
//
////                    _event.emit(CalendarEvent.NavigationSuccessAppointment(result.data.id, clinicId))
//                }
//                is DataResult.Error -> {
//                    Log.d("TEST_CASE", "Mã lỗi: ${result.target} - Lý do: ${result.message}")
//                    _state.value = _state.value.copy(
//                        isName = (result.target == ErrorTarget.NAME),
//                        isPhone = (result.target == ErrorTarget.PHONE),
//                        nameError = if (result.target == ErrorTarget.NAME || result.target == ErrorTarget.GENERAL) result.message else "",
//                        phoneError = if (result.target == ErrorTarget.PHONE || result.target == ErrorTarget.GENERAL) result.message else "",
//                    )
//
//                    return@launch
//                }
//            }
//            }
//        }
//    }
}