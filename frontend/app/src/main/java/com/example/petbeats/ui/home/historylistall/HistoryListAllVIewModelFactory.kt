package com.example.petbeats.ui.home.historylistall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petbeats.data.local.dao.HistoryDao
import com.example.petbeats.data.remote.sharepreference.TokenManager
import com.example.petbeats.data.repository.HomeRepository

class HistoryListAllVIewModelFactory(
    private val repository: HomeRepository,
    private val historyDao: HistoryDao,
    private val tokenManager: TokenManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryListAllViewModel(repository, historyDao, tokenManager) as T
    }
}