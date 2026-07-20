package com.vetpet.petbeats.ui.home_user.informationroom

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeUserRepository

class InformationRoomViewModelFactory(
    private val repository: HomeUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return InformationRoomViewModel(repository) as T
    }
}