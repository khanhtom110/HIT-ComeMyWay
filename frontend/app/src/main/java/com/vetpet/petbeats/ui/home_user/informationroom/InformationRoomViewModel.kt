package com.vetpet.petbeats.ui.home_user.informationroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.ClinicIdRequest
import com.vetpet.petbeats.data.repository.HomeUserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InformationRoomViewModel(
    private val repository: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow(InformationRoomState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<InformationRoomEvent>()
    val event = _event.asSharedFlow()

    fun resultClick() {
        viewModelScope.launch {
            _event.emit(InformationRoomEvent.NavigationResultSearch)
        }
    }

    fun calendarClick(clinicId: Int) {
        val id = _state.value.id

        viewModelScope.launch {
            _event.emit(InformationRoomEvent.NavigationCalendar(id, clinicId))
        }
    }

    fun onLatiLong(latitude: Double, longitude: Double) {
        _state.value = _state.value.copy(latitude = latitude, longitude = longitude)
    }


    fun onInformationList(clinicId: Int) {
        viewModelScope.launch {
            val latitude = _state.value.latitude
            val longitude = _state.value.longitude
            val request = ClinicIdRequest(id = clinicId, latitude, longitude)
            val result = repository.clinicid(request)

            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        id = data.id,
                        thumbnailUrl = data.thumbnailUrl,
                        name = data.name,
                        isOperating = data.isOperating,
                        rating = data.rating,
                        address = data.address,
                        openTime = data.openTime,
                        closeTime = data.closeTime,
                        description = data.description,
                        phone = data.phone,
                        services = data.services,
                        mapLink = data.mapLink,
                        distance = data.distance
                    )

                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy()
                }
            }
        }
    }
}