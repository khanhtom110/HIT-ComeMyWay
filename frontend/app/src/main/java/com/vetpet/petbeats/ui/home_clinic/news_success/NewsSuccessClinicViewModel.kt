package com.vetpet.petbeats.ui.home_clinic.news_success

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsSuccessClinicViewModel: ViewModel() {
    private val _state = MutableStateFlow(NewsSuccessClinicState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<NewsSuccessClinicEvent>()
    val event = _event.asSharedFlow()

    init {
        onInformationList()
    }


    fun newsPostClick() {
        viewModelScope.launch {
            _event.emit(NewsSuccessClinicEvent.NavigationNewsPost)
        }
    }
    fun newsClick() {
        viewModelScope.launch {
            _event.emit(NewsSuccessClinicEvent.NavigationNews)
        }
    }


    fun onInformationList() {
        viewModelScope.launch {

        }
    }



}