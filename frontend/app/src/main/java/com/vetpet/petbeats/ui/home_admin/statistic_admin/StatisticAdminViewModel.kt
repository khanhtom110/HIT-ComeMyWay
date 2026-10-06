package com.vetpet.petbeats.ui.home_admin.statistic_admin

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class StatisticAdminViewModel: ViewModel() {
    private val _state = MutableStateFlow(StatisticAdminState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<StatisticAdminEvent>()
    val event = _event.asSharedFlow()
}