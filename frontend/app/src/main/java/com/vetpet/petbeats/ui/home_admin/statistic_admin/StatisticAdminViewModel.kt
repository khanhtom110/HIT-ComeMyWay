package com.vetpet.petbeats.ui.home_admin.statistic_admin

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_admin.response.StatisticResponse
import com.vetpet.petbeats.data.remote.realtime.StatisticsHttpException
import com.vetpet.petbeats.data.repository.repository_nodejs.HomeAdminRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject
import kotlin.coroutines.cancellation.CancellationException

@HiltViewModel
class StatisticAdminViewModel @Inject constructor(
    private val repository: HomeAdminRepository
): ViewModel() {
    private val _state = MutableStateFlow(StatisticAdminState())
    val state = _state.asStateFlow()


    suspend fun observeStatistics() {
        while (currentCoroutineContext().isActive) {
            Log.d("STATISTICS", "Bắt đầu gọi API thống kê")

            try {
                val result = repository.statisticAdmin()

                when (result) {
                    is DataResult.Success -> {
                        val data = result.data

                        Log.d(
                            "STATISTICS",
                            "SUCCESS: active=${data.activeClinics}, " +
                                    "inactive=${data.inactiveClinics}, " +
                                    "users=${data.totalUsers}"
                        )

                        applyStatistics(data, connected = false)
                    }

                    is DataResult.Error -> {
                        Log.e(
                            "STATISTICS",
                            "ERROR: ${result.message}"
                        )

                        _state.value = _state.value.copy(
                            errorMessage = result.message
                        )
                    }
                }
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("STATISTICS", "Không tải được thống kê", e)

                _state.value = _state.value.copy(
                    errorMessage = e.message
                        ?: "Không tải được thống kê"
                )
            }

            delay(10_000L)
        }
    }


    private fun applyStatistics(data: StatisticResponse, connected: Boolean) {
        _state.value = _state.value.copy(
            activeClinics = data.activeClinics,
            inactiveClinics = data.inactiveClinics,
            totalUsers = data.totalUsers,
            isRealtimeConnected = connected,
            errorMessage = ""
        )
    }
}