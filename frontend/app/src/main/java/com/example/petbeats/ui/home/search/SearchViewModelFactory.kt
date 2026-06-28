package com.example.petbeats.ui.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petbeats.data.local.dao.HistoryDao

class SearchViewModelFactory(
    private val historyDao: HistoryDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(historyDao) as T
    }
}