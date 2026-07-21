package com.vetpet.petbeats.ui.home_clinic.scheduletoday

import androidx.lifecycle.ViewModel
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ScheduleTodayViewModel(
    private val repository: HomeClinicRepository
): ViewModel() {
    private val _state = MutableStateFlow(ScheduleTodayState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ScheduleTodayEvent>()
    val event = _event.asSharedFlow()
}