package com.example.petbeats.ui.home.book

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.remote.model.calendar.home.request.TakeBookingRequest
import com.example.petbeats.data.remote.model.calendar.home.response.TakeBookingResponse
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.ui.home.book.adapter.BookChild
import com.example.petbeats.ui.home.book.adapter.BookChildState
import com.example.petbeats.ui.home.historyBook.HistoryBookEvent
import com.example.petbeats.ui.home.search.adapterhint.HintChild
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.collections.map

class BookViewModel(
    private val repository: HomeRepository
): ViewModel() {
    private val _state = MutableStateFlow(BookState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<BookEvent>()
    val event = _event.asSharedFlow()
    
    fun searchClick() {
        viewModelScope.launch {
            _event.emit(BookEvent.NavigationSearch)
        }
    }

    fun historyBookClick() {
        viewModelScope.launch {
            _event.emit(BookEvent.NavigationHistoryBook)
        }
    }

    fun itemClickBookAppointment(id: Int, clinicId: Int) {
        viewModelScope.launch {
            _event.emit(BookEvent.NavigationBookingAppointment(id, clinicId))
        }
    }

    fun onBookingList() {
        viewModelScope.launch {
            val result = repository.takeAppointment()

            when (result) {
                is DataResult.Success -> {
                    val apiDataList = result.data

                    val showList = apiDataList.map { list ->
                        val mapStatus = when (list.status) {
                            "PENDING" -> BookChildState.PENDING
                            "SUCCESS" -> BookChildState.SUCCESS
                            "CANCELLED" -> BookChildState.CANCELLED
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