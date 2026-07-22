package com.vetpet.petbeats.ui.home_clinic.appointmentschedule

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import com.vetpet.petbeats.ui.home_clinic.appointmentschedule.adapter.AppointmentChild
import com.vetpet.petbeats.ui.home_user.book.adapter.BookChild
import com.vetpet.petbeats.ui.home_user.book.adapter.BookChildState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.Int

class AppointmentScheduleViewModel(
    private val repository: HomeClinicRepository
): ViewModel() {
    private val _state = MutableStateFlow(AppointmentScheduleState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<AppointmentScheduleEvent>()
    val event = _event.asSharedFlow()

    fun scheduleList() {
        viewModelScope.launch {
            _event.emit(AppointmentScheduleEvent.NavigationScheduleList)
        }
    }



    fun onWaitClick() {
        _state.value = _state.value.copy(isWait = true, isRefuse = false, isReceive = false)
    }
    fun onReceiveClick() {
        _state.value = _state.value.copy(isWait = false, isRefuse = false, isReceive = true)
    }
    fun onRefuseClick() {
        _state.value = _state.value.copy(isWait = false, isRefuse = true, isReceive = false)
    }



    fun itemDetailClick(id: Int) {
        viewModelScope.launch {

        }
    }
    fun itemReceiveClick(id: Int) {

    }
    fun itemRefuseClick(id: Int) {

    }



    //Api hiển thị wait list
    fun onAppointmentWaitList() {
        viewModelScope.launch {
            val result = repository.pendingAppointment()

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data

                    val showList = apiDataList.map { list ->
                        AppointmentChild(
                            id = list.id,
                            image = list.petType,
                            petName = list.petType,
                            user = list.fullName,
                            clinic = list.bookingType,
                            date = list.appointmentDate,
                            time = list.appointmentTime
                        )
                    }

                    _state.value = _state.value.copy(listAppointmentChild = showList)
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(listAppointmentChild = emptyList())
                }
            }
        }

    }

    //Api hiển thị refuse list
    fun onAppointmentRefuseList() {
        viewModelScope.launch {
            val result = repository.pendingAppointment()

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data

                    val showList = apiDataList.map { list ->
                        AppointmentChild(
                            id = list.id,
                            image = list.petType,
                            petName = list.petType,
                            user = list.fullName,
                            clinic = list.bookingType,
                            date = list.appointmentDate,
                            time = list.appointmentTime
                        )
                    }

                    _state.value = _state.value.copy(listAppointmentChild = showList)
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(listAppointmentChild = emptyList())
                }
            }
        }
    }

    //Api hiển thị receive list
    fun onAppointmentReceiveList() {
        viewModelScope.launch {
            val result = repository.pendingAppointment()

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data

                    val showList = apiDataList.map { list ->
                        AppointmentChild(
                            id = list.id,
                            image = list.petType,
                            petName = list.petType,
                            user = list.fullName,
                            clinic = list.bookingType,
                            date = list.appointmentDate,
                            time = list.appointmentTime
                        )
                    }

                    _state.value = _state.value.copy(listAppointmentChild = showList)
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(listAppointmentChild = emptyList())
                }
            }
        }
    }

}