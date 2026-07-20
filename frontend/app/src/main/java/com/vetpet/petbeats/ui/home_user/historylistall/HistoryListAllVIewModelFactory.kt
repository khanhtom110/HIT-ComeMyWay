package com.vetpet.petbeats.ui.home_user.historylistall

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.local.dao.HistoryDao
import com.vetpet.petbeats.data.remote.sharepreference.TokenManager
import com.vetpet.petbeats.data.repository.HomeUserRepository

class HistoryListAllVIewModelFactory(
    private val repository: HomeUserRepository,
    private val historyDao: HistoryDao,
    private val tokenManager: TokenManager
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HistoryListAllViewModel(repository, historyDao, tokenManager) as T
    }
}