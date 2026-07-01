package com.example.petbeats.ui.home.informationroom

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bumptech.glide.Glide
import com.example.petbeats.core.base.DataResult
import com.example.petbeats.data.remote.model.calendar.home.request.ClinicIdRequest
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.ui.home.search.adapterhint.HintChild
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class InformationRoomViewModel(
    private val repository: HomeRepository
): ViewModel() {
    private val _state = MutableStateFlow(InformationRoomState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<InformationRoomEvent>()
    val event = _event.asSharedFlow()

    fun resultClick() {
        viewModelScope.launch {
            _event.emit(InformationRoomEvent.NavigationResultSearch)
        }
    }


    fun onInformationList(id: Int) {
        viewModelScope.launch {
            val request = ClinicIdRequest(id = id)
            val result = repository.clinicid(request)

            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        thumbnailUrl = data.thumbnailUrl,
                        name = data.name,
                        isOperating = data.isOperating,
                        rating = data.rating,
                        address = data.address,
                        openTime = data.openTime,
                        closeTime = data.closeTime,
                        description = data.description,
                        phone = data.phone,
                        services = data.services ?: emptyList()
                    )

                }
                is DataResult.Error -> {
                    _state.value = _state.value.copy()
                }
            }
        }
    }
}