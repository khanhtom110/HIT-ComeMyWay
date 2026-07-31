package com.vetpet.petbeats.ui.home_user.locket

import androidx.lifecycle.ViewModel
import com.vetpet.petbeats.data.repository.HomeUserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

class LocketViewModel(
    private val repository: HomeUserRepository,
): ViewModel() {
    private val _state = MutableStateFlow(LocketState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<LocketEvent>()
    val event = _event.asSharedFlow()



}