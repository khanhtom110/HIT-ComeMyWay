package com.vetpet.petbeats.ui.home_admin.clinic_detail_admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.ClinicIdRequest
import com.vetpet.petbeats.data.repository.repository_springboot.HomeUserRepository
import com.vetpet.petbeats.ui.home_admin.list_appointment_admin.ListAppointmentAdminEvent
import com.vetpet.petbeats.ui.home_admin.list_appointment_admin.ListAppointmentAdminState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ClinicDetailAdminViewModel @Inject constructor(
    private val repository: HomeUserRepository
): ViewModel()  {
    private val _state = MutableStateFlow(ClinicDetailAdminState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ClinicDetailAdminEvent>()
    val event = _event.asSharedFlow()


    fun listAppointmentClick() {
        viewModelScope.launch {
            _event.emit(ClinicDetailAdminEvent.NavigationListAppointment)
        }
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
                        imgClinic = data.thumbnailUrl,
                        nameClinic = data.name,
                        address = data.address,
                        openTime = data.openTime,
                        closeTime = data.closeTime,
                        description = data.description,
                        phone = data.phone,
                        map = data.mapLink,
                        services = data.services,
                    )
                }
                is DataResult.Error -> {
                    return@launch
                }
            }
        }
    }

}