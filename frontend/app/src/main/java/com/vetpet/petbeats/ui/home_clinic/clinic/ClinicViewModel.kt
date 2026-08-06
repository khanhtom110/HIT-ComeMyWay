package com.vetpet.petbeats.ui.home_clinic.clinic

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.LogoutRequest
import com.vetpet.petbeats.data.remote.sharepreference.TokenManager
import com.vetpet.petbeats.data.repository.HomeClinicRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ClinicViewModel(
    private val repository: HomeClinicRepository,
    private val tokenManager: TokenManager
): ViewModel() {
    private val _state = MutableStateFlow(ClinicState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<ClinicEvent>()
    val event = _event.asSharedFlow()

    fun editInformationClick() {
        viewModelScope.launch {
            _event.emit(ClinicEvent.NavigationEditInformation)
        }
    }

    fun editPasswordClick() {
        viewModelScope.launch {
            _event.emit(ClinicEvent.NavigationEditPassword)
        }
    }

    fun onInformation() {
        viewModelScope.launch {
            viewModelScope.launch {
                val result = repository.getProfile()

                when (result) {
                    is DataResult.Success -> {
                        val data = result.data

                        _state.value = _state.value.copy(
                            id = data.id,
                            image = data.thumbnailUrl,
                            name = data.name,
                            phone = data.phone,
                            address = data.address,
                            link = data.mapLink,
                            openTime = data.openTime,
                            closeTime = data.closeTime,
                            state = data.description,
                            services = data.services,
                        )
                    }
                    is DataResult.Error -> {
                        Log.d("TEST_CASE", "lỗi trả: ${result.target} và message ${result.message}")

                        _state.value = _state.value.copy()
                        return@launch
                    }
                }
            }
        }
    }


    fun onLogoutClick() {
        viewModelScope.launch {
            _event.emit(ClinicEvent.NavigationLogin)
        }
    }

}