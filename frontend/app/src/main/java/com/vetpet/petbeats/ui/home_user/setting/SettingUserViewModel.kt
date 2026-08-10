package com.vetpet.petbeats.ui.home_user.setting
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.LogoutRequest
import com.vetpet.petbeats.data.remote.sharepreference.TokenManager
import com.vetpet.petbeats.data.repository.HomeUserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class SettingUserViewModel(
    private val repository: HomeUserRepository,
    private val tokenManager: TokenManager
): ViewModel() {
    private val _state = MutableStateFlow(SettingUserState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<SettingUserEvent>()
    val event = _event.asSharedFlow()

    fun editInformationClick() {
        val id = _state.value.id

        viewModelScope.launch {
            _event.emit(SettingUserEvent.NavigationEditInformationSetting(id))
        }
    }
    fun editPasswordClick() {
        viewModelScope.launch {
            _event.emit(SettingUserEvent.NavigationEditPasswordSetting)
        }
    }



    fun onProfile() {
        viewModelScope.launch {
            val result = repository.profile()

            when (result) {
                is DataResult.Success -> {
                    val data = result.data

                    _state.value = _state.value.copy(
                        id = data.id,
                        name = data.fullName.orEmpty(),
                        phone = data.phone.orEmpty(),
                        address = data.homeAddress.orEmpty(),
                        image = data.avatar.orEmpty(),
                        email = data.email,
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



    fun onLogoutClick() {
        viewModelScope.launch {
            val refreshToken = tokenManager.getRefreshToken() ?: ""
            val accessToken = tokenManager.getAccessToken() ?: ""

            val headerToken = "Bearer $accessToken"
            val request = LogoutRequest(refreshToken)

            val result = repository.logoutUser(headerToken, request)

            when (result) {
                is DataResult.Success -> {
                    tokenManager.clearTokens()
                    _event.emit(SettingUserEvent.NavigationLogin)
                }
                is DataResult.Error -> {
                    Log.d("TEST_CASE", "lỗi trả: ${result.target} và message ${result.message}")
                    return@launch
                }
            }
        }
    }

}