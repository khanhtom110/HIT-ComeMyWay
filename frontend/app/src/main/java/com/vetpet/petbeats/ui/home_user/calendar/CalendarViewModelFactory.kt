package com.vetpet.petbeats.ui.home_user.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeUserRepository

class CalendarViewModelFactory(
    private val repository: HomeUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalendarViewModel(repository) as T
    }
}