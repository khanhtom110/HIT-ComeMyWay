package com.vetpet.petbeats.ui.home_user.chatbot

import com.vetpet.petbeats.ui.home_user.book.adapter.BookChild

sealed class ChatbotState {
    data class UserMessage(val message: String) : ChatbotState()
    data class BotMessage(val message: String) : ChatbotState()


    data class BotSuggest(val message: String, val clinic: List<BookChild>) : ChatbotState()


    //Trạng thái chờ
    object BotTyping: ChatbotState()
}