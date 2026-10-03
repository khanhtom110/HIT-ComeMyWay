package com.vetpet.petbeats.ui.home_clinic.news_post

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NewsPostClinicViewModel: ViewModel() {
    private val _state = MutableStateFlow(NewsPostClinicState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<NewsPostClinicEvent>()
    val event = _event.asSharedFlow()

    fun newsClinicClick() {
        viewModelScope.launch {
            _event.emit(NewsPostClinicEvent.NavigationNewsClinic)
        }
    }


    fun checkImageClickTrue() {
        _state.value = _state.value.copy(imagePet = true)
    }
    fun checkImageClickFalse() {
        _state.value = _state.value.copy(imagePet = false)
    }



    fun onNewsPostClinicClick() {
        viewModelScope.launch {

        }
    }
}