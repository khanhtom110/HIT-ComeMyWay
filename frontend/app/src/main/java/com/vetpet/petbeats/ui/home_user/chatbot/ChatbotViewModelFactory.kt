package com.vetpet.petbeats.ui.home_user.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.vetpet.petbeats.data.repository.HomeUserRepository

class ChatbotViewModelFactory(
    private val repository: HomeUserRepository
): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return ChatbotViewModel(repository) as T
    }
}