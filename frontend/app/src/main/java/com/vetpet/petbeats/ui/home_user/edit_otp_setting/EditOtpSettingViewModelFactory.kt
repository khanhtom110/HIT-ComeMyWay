package com.vetpet.petbeats.ui.home_user.edit_otp_setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.AuthRepository
import com.vetpet.petbeats.data.repository.HomeUserRepository
import com.vetpet.petbeats.ui.auth.otp.OtpViewModel

class EditOtpSettingViewModelFactory(
    private val repository: HomeUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditOtpSettingViewModel(repository) as T
    }
}