package com.vetpet.petbeats.ui.home_clinic.news

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@HiltViewModel
class NewsClinicViewModel @Inject constructor(

): ViewModel() {
    private val _state = MutableStateFlow(NewsClinicState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<NewsClinicEvent>()
    val event = _event.asSharedFlow()
}