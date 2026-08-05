package com.vetpet.petbeats.ui.home_clinic.clinic

import androidx.lifecycle.ViewModel
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class ClinicViewModel(
    private val repository: HomeClinicRepository
): ViewModel() {
    private val _state = MutableStateFlow(ClinicState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ClinicEvent>()
    val event = _event.asSharedFlow()
}