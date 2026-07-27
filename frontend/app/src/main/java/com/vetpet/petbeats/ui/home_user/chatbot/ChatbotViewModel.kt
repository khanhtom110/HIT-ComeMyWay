package com.vetpet.petbeats.ui.home_user.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.ChatRequest
import com.vetpet.petbeats.data.repository.HomeUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ChatbotViewModel(
    private val repository: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow<List<ChatbotState>>(emptyList())
    val state = _state.asStateFlow()





    fun onChatBot(message: String) {
        viewModelScope.launch {
            val request = ChatRequest(message)
            val result = repository.chatBot(request)


        }
    }


}