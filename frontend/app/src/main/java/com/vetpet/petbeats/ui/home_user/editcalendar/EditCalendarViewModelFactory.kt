package com.vetpet.petbeats.ui.home_user.editcalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeUserRepository

class EditCalendarViewModelFactory(
    private val repository: HomeUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditCalendarViewModel(repository) as T
    }
}