package com.example.petbeats.ui.home.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petbeats.data.repository.HomeRepository

class CalendarViewModelFactory(
    private val repository: HomeRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return CalendarViewModel(repository) as T
    }
}