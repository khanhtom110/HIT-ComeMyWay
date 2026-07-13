package com.example.petbeats.ui.home.editcalendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petbeats.data.repository.HomeRepository
import com.example.petbeats.ui.home.calendar.CalendarViewModel

class EditCalendarViewModelFactory(
    private val repository: HomeRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return EditCalendarViewModel(repository) as T
    }
}