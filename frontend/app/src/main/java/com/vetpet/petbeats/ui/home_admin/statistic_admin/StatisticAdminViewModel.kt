package com.vetpet.petbeats.ui.home_admin.statistic_admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.repository.repository_nodejs.HomeAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StatisticAdminViewModel @Inject constructor(
    private val repository: HomeAdminRepository
): ViewModel() {
    private val _state = MutableStateFlow(StatisticAdminState())
    val state = _state.asStateFlow()



    fun onQuantityClick() {
        viewModelScope.launch {
            val result = repository.statisticAdmin()

            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        activeClinics = data.activeClinics,
                        inactiveClinics = data.inactiveClinics,
                        totalUsers = data.totalUsers
                    )
                }
                is DataResult.Error -> {
                    return@launch
                }
            }
        }
    }


}