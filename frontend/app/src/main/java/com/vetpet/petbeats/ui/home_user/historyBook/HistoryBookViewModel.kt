package com.vetpet.petbeats.ui.home_user.historyBook

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.home_user.book.adapter.BookChild
import com.vetpet.petbeats.ui.home_user.book.adapter.BookChildState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class HistoryBookViewModel(
    private val repository: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow(HistoryBookState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<HistoryBookEvent>()
    val event = _event.asSharedFlow()

    fun bookClick() {
        viewModelScope.launch {
            _event.emit(HistoryBookEvent.NavigationBook)
        }
    }

    fun itemClickBookAppointment(id: Int, clinicId: Int) {
        viewModelScope.launch {
            _event.emit(HistoryBookEvent.NavigationBookingAppointment(id, clinicId))
        }
    }


    fun onHistoryBookingList() {
        viewModelScope.launch {
            val result = repository.takeAppointment()

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data

                    val showList = apiDataList.map { list ->
                        val mapStatus = when (list.status) {
                            "PENDING" -> BookChildState.PENDING
                            "SUCCESS" -> BookChildState.CONFIRMED
                            "CANCELLED" -> BookChildState.REJECTED
                            else -> BookChildState.PENDING
                        }

                        BookChild(
                            id = list.id,
                            clinicId = list.clinicId,
                            thumbnailUrl = list.thumbnailUrl,
                            nameClinic = list.name,
                            address = list.address,
                            calendar = list.appointmentDate,
                            time = list.appointmentTime,
                            status = mapStatus
                        )
                    }

                    _state.value = _state.value.copy(listBook = showList)
                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy(listBook = emptyList())
                }
            }
        }
    }
}