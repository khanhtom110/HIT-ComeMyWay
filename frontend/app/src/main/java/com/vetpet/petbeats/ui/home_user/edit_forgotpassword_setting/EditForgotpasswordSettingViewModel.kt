package com.vetpet.petbeats.ui.home_user.edit_forgotpassword_setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.repository.AuthRepository
import com.vetpet.petbeats.data.repository.ErrorTarget
import com.vetpet.petbeats.data.remote.model.calendar.auth.request.ForgotPasswordRequest
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.auth.forgotpassword_user.ForgotPasswordEvent
import com.vetpet.petbeats.ui.auth.forgotpassword_user.ForgotPasswordState
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.annotation.meta.When

class EditForgotpasswordSettingViewModel(
    private val repository: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow(EditForgotpasswordSettingState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<EditForgotpasswordSettingEvent>()
    val event = _event.asSharedFlow()

    fun onEmailChange(email: String) {
        _state.value = _state.value.copy(email = email, isEmail = false)
    }

    fun settingClick() {
        viewModelScope.launch {
            _event.emit(EditForgotpasswordSettingEvent.NavigationSetting)
        }
    }

    fun onOtpClick() {
        viewModelScope.launch {
            val email = _state.value.email.trim()


            val request = ForgotPasswordRequest(email)
            val result = repository.forgotPasswordUser(request)


            when (result) {
                is DataResult.Success -> {
                    _state.value = _state.value.copy(isEmail = false, emailError = "")

                    _event.emit(EditForgotpasswordSettingEvent.NavigationOTPSendEmail(email))
                }

                is DataResult.Error -> {
                    _state.value = _state.value.copy(
                        isEmail = (result.target == ErrorTarget.EMAIL || result.target ==  ErrorTarget.GENERAL),
                        emailError = if (result.target == ErrorTarget.EMAIL || result.target ==  ErrorTarget.GENERAL) result.message else ""
                    )
                    return@launch
                }
            }
        }
    }
}