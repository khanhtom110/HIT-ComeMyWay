package com.example.petbeats.ui.home.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petbeats.data.local.dao.HistoryDao
import com.example.petbeats.data.remote.api.ApiHome
import com.example.petbeats.data.repository.HomeRepository

class SearchViewModelFactory(
    private val repository: HomeRepository,
    private val historyDao: HistoryDao
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(repository, historyDao) as T
    }
}