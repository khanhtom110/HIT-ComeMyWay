package com.vetpet.petbeats.ui.home_admin.list_appointment_admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.data.repository.repository_springboot.AuthAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class ListAppointmentAdminViewModel @Inject constructor(
) : ViewModel() {
    private val _state = MutableStateFlow(ListAppointmentAdminState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ListAppointmentAdminEvent>()
    val event = _event.asSharedFlow()


    init {
        onReceiveClick()
    }


    fun addClinicClick() {
        viewModelScope.launch {
            _event.emit(ListAppointmentAdminEvent.NavigationAddClinic)
        }
    }





    fun onWaitClick() {
        _state.value = _state.value.copy(isWait = true, isReceive = false, isLoading = true, listAppointmentAdmin = emptyList())

        onAppointmentWaitAdminList()
    }
    fun onReceiveClick() {
        _state.value = _state.value.copy(isWait = false, isReceive = true, isLoading = true, listAppointmentAdmin = emptyList())

        onAppointmentReceiveAdminList()
    }


    fun itemDeleteAccountClick(id: Int) {
        viewModelScope.launch {

        }
    }
    fun itemLockAccountClick(id: Int) {
        viewModelScope.launch {

        }
    }



    fun onAppointmentWaitAdminList() {
        viewModelScope.launch {

        }
    }

    fun onAppointmentReceiveAdminList() {
        viewModelScope.launch {

        }
    }

}