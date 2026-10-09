package com.vetpet.petbeats.ui.home_admin.list_appointment_admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.repository.repository_springboot.AuthAdminRepository
import com.vetpet.petbeats.ui.home_admin.list_appointment_admin.adapter.AppointmentAdminChild
import dagger.hilt.android.lifecycle.HiltViewModel
import jakarta.inject.Inject
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.map
import kotlin.collections.plus

@HiltViewModel
class ListAppointmentAdminViewModel @Inject constructor(
    private val repository: AuthAdminRepository
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
        _state.value = _state.value.copy(isWait = true, isReceive = false, isLoading = true, listAppointmentAdmin = emptyList(), nextCursor = null)

        onAppointmentWaitAdminList()
    }
    fun onReceiveClick() {
        _state.value = _state.value.copy(isWait = false, isReceive = true, isLoading = true, listAppointmentAdmin = emptyList(), nextCursor = null)

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


    fun itemClinicDetailClick(clinicId: Int) {
        viewModelScope.launch {
            _event.emit(ListAppointmentAdminEvent.NavigationClinicDetail(clinicId))
        }
    }



    fun onAppointmentWaitAdminList() {
        val cursor = _state.value.nextCursor

        viewModelScope.launch {
            val result = repository.clinicList("inactive", "", 20, cursor)

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data
                    val apiData = apiDataList.content

                    val showList = apiData.map { list ->
                        AppointmentAdminChild(
                            id = list.urlId,
                            clinicId = list.clinicId,
                            nameAccount = list.name,
                        )
                    }

                    val currentList = _state.value.listAppointmentAdmin
                    val updatedList = currentList + showList

                    _state.value = _state.value.copy(isLoading = false, listAppointmentAdmin = updatedList, nextCursor = apiDataList.nextCursor)
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(isLoading = false, listAppointmentAdmin = emptyList())
                }
            }
        }
    }

    fun onAppointmentReceiveAdminList() {
        val cursor = _state.value.nextCursor

        viewModelScope.launch {
            val result = repository.clinicList("active", "", 20, cursor)

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data
                    val apiData = apiDataList.content

                    val showList = apiData.map { list ->
                        AppointmentAdminChild(
                            id = list.urlId,
                            clinicId = list.clinicId,
                            nameAccount = list.name
                        )
                    }

                    val currentList = _state.value.listAppointmentAdmin
                    val updatedList = currentList + showList

                    _state.value = _state.value.copy(isLoading = false, listAppointmentAdmin = updatedList, nextCursor = apiDataList.nextCursor)
                }
                is DataResult.Error -> {
                    Log.d("tesssst", "error= ${result.message}")

                    _state.value = _state.value.copy(isLoading = false, listAppointmentAdmin = emptyList())
                }
            }
        }
    }

}