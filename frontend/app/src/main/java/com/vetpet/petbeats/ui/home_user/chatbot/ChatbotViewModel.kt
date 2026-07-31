package com.vetpet.petbeats.ui.home_user.chatbot

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.vetpet.petbeats.core.base.DataResult
import com.vetpet.petbeats.data.remote.model.calendar.home_user.request.ChatRequest
import com.vetpet.petbeats.data.repository.HomeUserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class ChatbotViewModel(
    private val repository: HomeUserRepository
): ViewModel() {
    private val _state = MutableStateFlow(ChatbotState())
    val state = _state.asStateFlow()

    private val _event = MutableStateFlow<List<ChatbotEvent>>(emptyList())
    val event = _event.asStateFlow()



    fun onChatUserChange(chatUser: String) {
        _state.value = _state.value.copy(chatUser = chatUser, isChatUser = chatUser.isNotEmpty())
    }


    fun onChatBot() {
        val chatUser = _state.value.chatUser

        _state.value = _state.value.copy(chatUser = "", isChatUser = false, isLogo = true)

        val currentList = _event.value.toMutableList()
        currentList.add(ChatbotEvent.UserMessage(chatUser))
        currentList.add(ChatbotEvent.BotTyping)


        _event.value = currentList


        viewModelScope.launch {
            val request = ChatRequest(chatUser)
            val result = repository.chatBot(request)

            val updatedList = _event.value.toMutableList()
            updatedList.removeIf {
                it is ChatbotEvent.BotTyping
            }

            when (result) {
                is DataResult.Success -> {
                    val data = result.data
                    val aiMessage = data.aiResponse ?: ""
                    val clinic = data.recommendedClinics


                    if (clinic.isNullOrEmpty()) {
                        updatedList.add(ChatbotEvent.BotMessage(aiMessage))
                    } else {
                        updatedList.add(ChatbotEvent.BotSuggest(aiMessage, clinic))
                    }
                }
                is DataResult.Error -> {
                    updatedList.add(ChatbotEvent.BotMessage("Xin lỗi, vì đã xảy ra lỗi kết nối"))
                }
            }
            _event.value = updatedList.toList()
        }
    }
}