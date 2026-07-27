package com.vetpet.petbeats.ui.home_user.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.ChatRequest
import com.vetpet.petbeats.data.repository.HomeUserRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ChatbotViewModel(
    private val repository: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow(ChatbotState())
    val state = _state.asStateFlow()

    private val _event = MutableSharedFlow<List<ChatbotEvent>>()
    val event = _event.asSharedFlow()



    fun onChatUserChange(chatUser: String) {
        _state.value = _state.value.copy(chatUser = chatUser, isChatUser = chatUser.isNotEmpty())
    }


    fun onChatBot() {
        val chatUser = _state.value.chatUser


        val currentList = mutableListOf<ChatbotEvent>()
        currentList.add(ChatbotEvent.UserMessage(chatUser))
        currentList.add(ChatbotEvent.BotTyping)

        _state.value = _state.value.copy(chatUser = "", isChatUser = false, isLogo = true)


//        viewModelScope.launch {
//            val request = ChatRequest(chatUser)
//            val result = repository.chatBot(request)
//
//            when (result) {
//                is DataResult.Success -> {
//
//
//
//                }
//                is DataResult.Error -> {
//
//                }
//            }
//        }
    }
}