package com.example.petbeats.ui.home.calendar

import androidx.lifecycle.ViewModel
import com.example.petbeats.data.repository.HomeRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class CalendarViewModel(
    private val repository: HomeRepository
): ViewModel() {
    private var _state = MutableStateFlow(CalendarState())
    val state = _state.asStateFlow()

    private var _event = MutableSharedFlow< CalendarEvent>()
    val event = _event.asSharedFlow()
}