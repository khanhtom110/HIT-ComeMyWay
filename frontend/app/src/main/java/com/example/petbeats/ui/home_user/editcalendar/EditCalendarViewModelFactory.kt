package com.example.petbeats.ui.home_user.editcalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petbeats.data.repository.HomeRepository

class EditCalendarViewModelFactory(
    private val repository: HomeRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditCalendarViewModel(repository) as T
    }
}